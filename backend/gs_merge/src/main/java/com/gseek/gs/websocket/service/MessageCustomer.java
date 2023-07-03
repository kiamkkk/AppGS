package com.gseek.gs.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.websocket.message.BaseMessage;
import com.gseek.gs.websocket.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Phak
 * @since 2023/6/30-12:06
 * @deprecated 直接把rabbitmq作为消息代理后不需要publisher-customer体系
 */
@Component
@Slf4j
public class MessageCustomer implements MessageListener {

    @Autowired
    private SimpMessagingTemplate operations;
    @Autowired
    ObjectMapper objectMapper;


    public void sendMessage(BaseMessage bm) throws JsonProcessingException {
        MessageType type=MessageType.getTypeByName(bm.getType());
        switch (type) {
            case ANNOUNCE -> {
                operations.convertAndSend("/topic/announcement", bm);
            }
            case NOTICE -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/notice/general", bm);
            }
            case DELIVERY -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/notice/delivery", bm);
            }
            case BLACKLIST -> {
                operations.convertAndSendToUser(bm.getToUserId()+"","/remind/blacklist/",bm);
            }
            case CHAT_PIC,CHAT_TEXT-> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/chat", bm);
            }
            default -> {
                log.error("无法识别消息类别，消息内容为:\n{}", bm);
            }
        }
    }


    @Override
    public void onMessage(Message message) {
        try {
            sendMessage(objectMapper.readValue(message.getBody(), BaseMessage.class));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
