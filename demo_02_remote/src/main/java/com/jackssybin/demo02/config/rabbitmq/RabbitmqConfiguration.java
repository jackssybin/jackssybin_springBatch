package com.jackssybin.demo02.config.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
    @Autowired
    PropertyConfiguration propertyConfiguration
            =new PropertyConfiguration();

    @Bean(name="rabbitmqConnectionFactory")
    public ConnectionFactory rabbitmqConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(propertyConfiguration.getHost(), propertyConfiguration.getPort());
        connectionFactory.setUsername(propertyConfiguration.getUsername());
        connectionFactory.setPassword(propertyConfiguration.getPassword());
        connectionFactory.setVirtualHost(propertyConfiguration.getVirtualHost());
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(propertyConfiguration.getConnRecvThreads());
//        executor.initialize();
//        connectionFactory.setExecutor(executor);
        connectionFactory.setChannelCacheSize(propertyConfiguration.getChannelCacheSize());

        //发送confirm模式需要 增加如下配置
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
//        rabbitTemplate.setConfirmCallback(rabbitMQConfirmAndReturn);
//        rabbitTemplate.setReturnCallback(rabbitMQConfirmAndReturn);
        //Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者，为false时匹配不到会直接被丢弃
        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    public Connection getConnection(ConnectionFactory connectionFactory){
        try {
            return connectionFactory.createConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Channel getChannel(Connection connection){
        try {
            return connection.createChannel(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  void closeConnectionAndChannel(Connection connection, Channel channel){
        try {
            if (connection != null) connection.close();
            if (channel != null)channel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }








}
