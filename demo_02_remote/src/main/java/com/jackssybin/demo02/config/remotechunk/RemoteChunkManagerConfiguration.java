package com.jackssybin.demo02.config.remotechunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@EnableIntegration
@Profile({"remoteChunkManager","remoteChunkMixed"})
public class RemoteChunkManagerConfiguration {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Autowired
    private RemoteChunkingManagerStepBuilderFactory managerStepBuilderFactory;

    @Bean
    public Job remoteChunkingJob() {
        return this.jobBuilderFactory.get("remoteChunkingJob")
                .start(remoteChunkManagerStep()).build();
    }

    @Bean
    public DirectChannel mangerRequests() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow managerOutboundFlow() {
        return IntegrationFlows.from(mangerRequests())
                .handle(Amqp.outboundAdapter(rabbitTemplate).routingKey("requests"))
                .get();
    }

    @Bean
    public QueueChannel managerReplies() {
        return new QueueChannel();
    }
    @Bean
    public IntegrationFlow managerInboundFlow(ConnectionFactory rabbitmqConnectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(rabbitmqConnectionFactory,"replies"))
                .channel(managerReplies())
                .get();
    }



    @Bean
    public TaskletStep remoteChunkManagerStep() {
        return this.managerStepBuilderFactory.get("remoteChunkManagerStep")
                .<Integer, Integer>chunk(3)
                .reader(itemReader())
                .outputChannel(mangerRequests())
                .inputChannel(managerReplies())
                .build();
    }


    @Bean
    public ListItemReader<Integer> itemReader() {
        return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5, 6));
    }
}
