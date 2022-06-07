package com.jackssybin.demo02.config.remotepartition.poll;

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
@Profile({"remotePartitionDBManager","remotePartitionDBMixed"})
public class RemotePartitionDBPollManagerConfiguration {

    private static final int GRID_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;

    private final RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;

    private final RabbitTemplate rabbitTemplate;

    public RemotePartitionDBPollManagerConfiguration(JobBuilderFactory jobBuilderFactory,
                                                     RabbitTemplate rabbitTemplate,
                                                     RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.managerStepBuilderFactory = managerStepBuilderFactory;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean  //定义channle
    public DirectChannel managerDBPollRequests() {
        return new DirectChannel();
    }
    @Bean  // 定义从channel内容经过amqp发送到requests队列中
    public IntegrationFlow managerDBPollOutboundFlow() {
        return IntegrationFlows.from(managerDBPollRequests())
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("requests"))
                .get();
    }

    // 定义远程分区step Manager端(master)
    @Bean
    public Step managerDBPollStep() {
        return this.managerStepBuilderFactory.get("managerDBPollStep")
                .partitioner("workerDBPollStep", new SimplePartitioner())
                .gridSize(GRID_SIZE)
                .outputChannel(managerDBPollRequests())
                .build();
    }


    @Bean
    public Job remotePartitioningDBPollJob() {
        return this.jobBuilderFactory
                .get("remotePartitioningDBPollJob")
                .start(managerDBPollStep())
                .build();
    }
}
