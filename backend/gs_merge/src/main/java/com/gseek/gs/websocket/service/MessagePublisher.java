package com.gseek.gs.websocket.service;

import com.gseek.gs.config.RabbitMQConfig;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.websocket.message.MessageType;
import com.gseek.gs.websocket.message.NoticeMessage;
import com.gseek.gs.websocket.message.chat.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Phak
 * @since 2023/6/30-12:28
 * @deprecated 直接把rabbitmq作为消息代理后不需要publisher-customer体系
 */
@Component
@Slf4j
public class MessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("chatRecordServiceImpl")
    private ChatRecordService chatRecordService;


    /**
     * 接收通知消息
     * - 包括：
     * - - 一般通知消息
     * - - 交货消息
     *
     * 发布通知消息
     * */
    public void receiveMessage(NoticeMessage message){
        MessageType type = MessageType.getTypeByName(message.getType());
        switch (type){
            case NOTICE -> {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC, RabbitMQConfig.KEY_NOTICE, message);
            }
            case DELIVERY -> {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC, RabbitMQConfig.KEY_DELIVERY, message);
            }
            default -> {
                log.warn("出现异常信息类型：{}", type.name());
            }
        }
    }

    /**
     * 接收合法的聊天消息
     * - todo 什么叫合法？？？
     * - - 被controller处理过的
     * - - 是用户自己发送的
     * - - 发送用户状态正常
     * - - - 没在黑名单里
     * - - - 没被对方拉黑
     * - - 发送与接收用户均存在
     *
     * 发送到rabbitMQ，储存聊天记录
     * */
    public void receiveMessage(ChatMessage message){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC, RabbitMQConfig.KEY_CHAT, message);
        chatRecordService.insertMessage(message);
    }

}
