package com.jackssybin.demo02.config.remotechunk;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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
@Profile({"remoteChunkWorker","remoteChunkMixed"})
public class RemoteChunkWorkerConfiguration {

    @Autowired
    private RemoteChunkingWorkerBuilder<Integer, Integer> remoteChunkingWorkerBuilder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public DirectChannel workerRequests() {
        return new DirectChannel();
    }
    @Bean
    public IntegrationFlow workerInboundFlow(ConnectionFactory rabbitmqConnectionFactory) {
        return IntegrationFlows
                .from(Amqp.inboundAdapter(rabbitmqConnectionFactory,"requests"))
                .channel(workerRequests()).get();
    }
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

    @Bean
    public ItemProcessor<Integer, Integer> itemProcessor() {
        return item -> {
            System.out.println("processing item " + item);
            return item;
        };
    }

    @Bean
    public ItemWriter<Integer> itemWriter() {
        return items -> {
            for (Integer item : items) {
                System.out.println("writing item " + item);
            }
        };
    }

    @Bean
    public IntegrationFlow workerIntegrationFlow() {
        return this.remoteChunkingWorkerBuilder
                .itemProcessor(itemProcessor())
                .itemWriter(itemWriter())
                .inputChannel(workerRequests())
                .outputChannel(workerReplies())
                .build();
    }
}
