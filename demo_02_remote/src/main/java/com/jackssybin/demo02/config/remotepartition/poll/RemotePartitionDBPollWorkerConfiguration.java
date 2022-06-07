package com.jackssybin.demo02.config.remotepartition.poll;

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
@Profile({"remotePartitionDBWorker","remotePartitionDBMixed"})
public class RemotePartitionDBPollWorkerConfiguration {

    private final RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;
    private final RabbitTemplate rabbitTemplate;
    public RemotePartitionDBPollWorkerConfiguration(RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory,
                                                    RabbitTemplate rabbitTemplate) {
        this.workerStepBuilderFactory = workerStepBuilderFactory;
        this.rabbitTemplate = rabbitTemplate;
    }

    // 定义消息接收channel
    @Bean
    public DirectChannel workerDBPollRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow workerDBPollInboundFlow(ConnectionFactory rabbitmqConnectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(
                        rabbitmqConnectionFactory,"requests"))//从消息队列request来消息
                .channel(workerDBPollRequests()//执行完的消息内容，发送到channel
                ).get();
    }

    @Bean
    public Step workerDBPollStep() {
        return this.workerStepBuilderFactory
                .get("workerDBPollStep") //step名称
                .inputChannel(workerDBPollRequests()) // 接收channel
                .tasklet(workerDBPollStepTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet workerDBPollStepTasklet(@Value("#{stepExecutionContext['partition']}") String partition) {
        return (contribution, chunkContext) -> {
            System.out.println("workerDBPollStepTasklet-=processing " + partition);
            return RepeatStatus.FINISHED;
        };
    }
}
