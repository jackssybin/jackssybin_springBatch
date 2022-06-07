package com.jackssybin.demo02.config.remotepartition;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
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
@Profile({"remotePartitionManager","remotePartitionMixed"})
public class RemotePartitionAggManagerConfiguration {

    private static final int GRID_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;

    private final RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;

    private final RabbitTemplate rabbitTemplate;

    public RemotePartitionAggManagerConfiguration(JobBuilderFactory jobBuilderFactory,
                                                  RabbitTemplate rabbitTemplate,
                                                  RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.managerStepBuilderFactory = managerStepBuilderFactory;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public DirectChannel managerRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow managerOutboundFlow() {
        return IntegrationFlows.from(managerRequests())
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("requests"))
                .get();
    }

    /*
     * Configure inbound flow (replies coming from workers)
     */
    @Bean
    public DirectChannel managerReplies() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow managerInboundFlow(ConnectionFactory rabbitmqConnectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(
                        rabbitmqConnectionFactory,"replies"))
                .channel(managerReplies()).get();
    }

    /*
     * Configure the manager step
     */
    @Bean
    public Step managerStep() {
        return this.managerStepBuilderFactory.get("managerStep")
                .partitioner("workerStep", new SimplePartitioner())
                .gridSize(GRID_SIZE)
                .outputChannel(managerRequests())
                .inputChannel(managerReplies())
                .build();
    }

    @Bean
    public Job remotePartitioningJob() {
        return this.jobBuilderFactory
                .get("remotePartitioningJob")
                .start(managerStep())
                .build();
    }
}
