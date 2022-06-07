package com.jackssybin.demo02.config.remotepartition;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
@Profile({"remotePartitionWorker","remotePartitionMixed"})
public class RemotePartitionAggWorkerConfiguration {
    private final RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;
    private final RabbitTemplate rabbitTemplate;
    public RemotePartitionAggWorkerConfiguration(RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory,
                                                 RabbitTemplate rabbitTemplate) {
        this.workerStepBuilderFactory = workerStepBuilderFactory;
        this.rabbitTemplate = rabbitTemplate;
    }

    /*
     * Configure inbound flow (requests coming from the manager)
     */
    @Bean
    public DirectChannel workerRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow workerInboundFlow(ConnectionFactory rabbitmqConnectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(
                        rabbitmqConnectionFactory,"requests"))
                .channel(workerRequests()).get();
    }

    /*
     * Configure outbound flow (replies going to the manager)
     */
    @Bean
    public DirectChannel workerReplies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow workerOutboundFlow() {
        return IntegrationFlows.from(workerReplies())
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("replies"))
                .get();
    }

    /*
     * Configure the worker step
     */
    @Bean
    public Step workerStep() {
        return this.workerStepBuilderFactory
                .get("workerStep")
                .inputChannel(workerRequests())
                .outputChannel(workerReplies())
                .tasklet(tasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet tasklet(@Value("#{stepExecutionContext['partition']}") String partition) {
        return (contribution, chunkContext) -> {
            System.out.println("processing " + partition);
            return RepeatStatus.FINISHED;
        };
    }
}
