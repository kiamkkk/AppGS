package com.gseek.gs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq设置.
 *
 * @author Phak
 * @since 2023/5/15-17:18
 */
@Configuration
@EnableRabbit
@Slf4j
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.host}")
    public String host;
    @Value("${spring.rabbitmq.port}")
    public int port;
    @Value("${spring.rabbitmq.username}")
    public String username;
    @Value("${spring.rabbitmq.password}")
    public String password;
    @Value("${spring.rabbitmq.virtual-host}")
    public String virtualHost;

    public final static String EXCHANGE_TOPIC = "gseekExchange";
    /**
     * 绑定键
     */
    public final static String KEY_NOTICE = "notice";
    public final static String KEY_DELIVERY = "delivery";
    public final static String KEY_CHAT = "chat";
    public final static String KEY_BLACKLIST = "topic.blacklist";
    public final static String KEY_APPEAL = "topic.appeal";
    public final static String KEY_ADMIN = "topic.admin";
    /**
     * 队列
     * */
    public final static String QUEUE_NOTICE = "noticeQueue";
    public final static String QUEUE_DELIVERY = "deliveryQueue";
    public final static String QUEUE_CHAT = "chatQueue";
    public final static String QUEUE_BLACKLIST = "blacklistQueue";
    public final static String QUEUE_APPEAL = "appealQueue";
    public final static String QUEUE_ADMIN = "adminQueue";

    @Bean("gseekExchange")
    public TopicExchange gseekExchange() {
        return new TopicExchange(EXCHANGE_TOPIC,true,false);
    }

    @Bean("noticeQueue")
    public Queue noticeQueue() {
        return new Queue(RabbitMQConfig.QUEUE_NOTICE,true);
    }
    @Bean("deliveryQueue")
    public Queue deliveryQueue() {
        return new Queue(RabbitMQConfig.QUEUE_DELIVERY,true);
    }
    @Bean("chatQueue")
    public Queue chatQueue() {
        return new Queue(RabbitMQConfig.QUEUE_CHAT,true);
    }
    @Bean("blacklistQueue")
    public Queue blacklistQueue() {
        return new Queue(RabbitMQConfig.QUEUE_BLACKLIST,true);
    }
    @Bean("appealQueue")
    public Queue appealQueue() {
        return new Queue(RabbitMQConfig.QUEUE_APPEAL,true);
    }
    @Bean("adminQueue")
    public Queue adminQueue() {
        return new Queue(RabbitMQConfig.QUEUE_ADMIN,true);
    }

    @Bean
    public Binding noticeQueueBindingTopicExchange() {
        return BindingBuilder.bind(noticeQueue()).to(gseekExchange()).with(KEY_NOTICE);
    }
    @Bean
    public Binding deliveryQueueBindingTopicExchange() {
        return BindingBuilder.bind(deliveryQueue()).to(gseekExchange()).with(KEY_DELIVERY);
    }
    @Bean
    public Binding chatQueueBindingTopicExchange() {
        return BindingBuilder.bind(chatQueue()).to(gseekExchange()).with(KEY_CHAT);
    }
    @Bean
    public Binding blacklistQueueBindingTopicExchange() {
        return BindingBuilder.bind(chatQueue()).to(gseekExchange()).with(KEY_BLACKLIST);
    }
    @Bean
    public Binding appealQueueBindingTopicExchange() {
        return BindingBuilder.bind(chatQueue()).to(gseekExchange()).with(KEY_APPEAL);
    }
    @Bean
    public Binding adminQueueBindingTopicExchange() {
        return BindingBuilder.bind(chatQueue()).to(gseekExchange()).with(KEY_ADMIN);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean("customMessageContainer")
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(
                noticeQueue(), deliveryQueue(), chatQueue()
        );

        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(10);
        container.setConcurrentConsumers(7);
        //设置确认模式自动确认
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return container;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
