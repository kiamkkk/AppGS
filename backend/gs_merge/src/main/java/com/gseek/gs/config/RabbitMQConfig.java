package com.gseek.gs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Phak
 * @since 2023/5/15-17:18
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    public static String host;
    @Value("${spring.rabbitmq.port}")
    public static int port;
    @Value("${spring.rabbitmq.username}")
    public static String username;
    @Value("${spring.rabbitmq.password}")
    public static String password;
    @Value("${spring.rabbitmq.virtual-host}")
    public static String virtualHost;

    public final static String EXCHANGE_TOPIC = "topicExchange";
    /**
     * 绑定键
     */
    public final static String KEY_ANNOUNCE = "topic.announce";
    public final static String KEY_DELIVERY = "topic.delivery";
    public final static String KEY_GENERAL = "topic.general";
    public final static String KEY_CHAT = "topic.chat";
    /**
     * 队列
     * */
    public final static String QUEUE_ANNOUNCE = "announceQueue";
    public final static String QUEUE_DELIVERY = "deliveryQueue";
    public final static String QUEUE_GENERAL = "generalQueue";
    public final static String QUEUE_CHAT = "chatQueue";

    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_TOPIC,true,false);
    }

    @Bean("announceQueue")
    public Queue announceQueue() {
        return new Queue(RabbitMQConfig.QUEUE_ANNOUNCE,true);
    }
    @Bean("deliveryQueue")
    public Queue deliveryQueue() {
        return new Queue(RabbitMQConfig.QUEUE_DELIVERY,true);
    }
    @Bean("generalQueue")
    public Queue generalQueue() {
        return new Queue(RabbitMQConfig.QUEUE_GENERAL,true);
    }
    @Bean("chatQueue")
    public Queue chatQueue() {
        return new Queue(RabbitMQConfig.QUEUE_CHAT,true);
    }


    @Bean
    public Binding announceQueueBindingTopicExchange() {
        return BindingBuilder.bind(announceQueue()).to(topicExchange()).with(KEY_ANNOUNCE);
    }
    @Bean
    public Binding deliveryQueueBindingTopicExchange() {
        return BindingBuilder.bind(deliveryQueue()).to(topicExchange()).with(KEY_DELIVERY);
    }
    @Bean
    public Binding generalQueueBindingTopicExchange() {
        return BindingBuilder.bind(generalQueue()).to(topicExchange()).with(KEY_GENERAL);
    }
    @Bean
    public Binding chatQueueBindingTopicExchange() {
        return BindingBuilder.bind(chatQueue()).to(topicExchange()).with(KEY_CHAT);
    }


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("GseekHost");
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            //todo 消息确认后回调方法
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            //todo 回退消息处理
        });

        return rabbitTemplate;
    }
}
