package com.gseek.gs.config;

import com.gseek.gs.websocket.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class RabbitMQConfig {
    public static String host;
    public static int port;
    public static String username;
    public static String password;
    public static String virtualHost;

    public final static String EXCHANGE_TOPIC = "gseekExchange";
    /**
     * 绑定键
     */
    public final static String KEY_ANNOUNCE = "topic.announce";
    public final static String KEY_NOTICE = "topic.notice";
    public final static String KEY_DELIVERY = "topic.delivery";
    public final static String KEY_CHAT = "topic.chat";
    public final static String KEY_BLACKLIST = "topic.blacklist";
    public final static String KEY_APPEAL = "topic.appeal";
    public final static String KEY_ADMIN = "topic.admin";
    /**
     * 队列
     * */
    public final static String QUEUE_ANNOUNCE = "announceQueue";
    public final static String QUEUE_NOTICE = "noticeQueue";
    public final static String QUEUE_DELIVERY = "deliveryQueue";
    public final static String QUEUE_CHAT = "chatQueue";
    public final static String QUEUE_BLACKLIST = "blacklistQueue";
    public final static String QUEUE_APPEAL = "appealQueue";
    public final static String QUEUE_ADMIN = "adminQueue";

    @Autowired
    MessageService messageService;

    @Bean("gseekExchange")
    public TopicExchange gseekExchange() {
        return new TopicExchange(EXCHANGE_TOPIC,true,false);
    }

    @Bean("announceQueue")
    public Queue announceQueue() {
        return new Queue(RabbitMQConfig.QUEUE_ANNOUNCE,true);
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
    public Binding announceQueueBindingTopicExchange() {
        return BindingBuilder.bind(announceQueue()).to(gseekExchange()).with(KEY_ANNOUNCE);
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
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
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

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            //todo 回退消息处理
        });

        return rabbitTemplate;
    }

    /**
     * 接受消息的监听，这个监听会接受消息队列topicQueue的消息
     * 针对消费者配置
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(announceQueue(),noticeQueue(),deliveryQueue(),chatQueue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            byte[] body = message.getBody();
            String msg = new String(body);
            log.debug("rabbitmq收到消息:\n{}",msg);
            messageService.receiveMsg(msg);
            //确认消息成功消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.debug("消息处理成功！ 已经推送到websocket！");
        });
        return container;
    }

    @Value("${spring.rabbitmq.host}")
    public void setHost(String host) {
        RabbitMQConfig.host = host;
    }
    @Value("${spring.rabbitmq.port}")
    public void setPort(int port) {
        RabbitMQConfig.port = port;
    }
    @Value("${spring.rabbitmq.username}")
    public void setUsername(String username) {
        RabbitMQConfig.username = username;
    }
    @Value("${spring.rabbitmq.password}")
    public void setPassword(String password) {
        RabbitMQConfig.password = password;
    }
    @Value("${spring.rabbitmq.virtual-host}")
    public void setVirtualHost(String virtualHost) {
        RabbitMQConfig.virtualHost = virtualHost;
    }
}
