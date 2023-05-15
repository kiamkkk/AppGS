package com.gseek.gs.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.RabbitMQConfig;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.websocket.message.DeliveryMessage;
import com.gseek.gs.websocket.message.GeneralMessage;
import com.gseek.gs.websocket.message.chat.ChatMessage;
import com.gseek.gs.websocket.message.chat.ChatPicMessage;
import com.gseek.gs.websocket.message.chat.ChatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Phak
 * @since 2023/5/8-23:11
 */
@Slf4j
@RestController
public class MessageController {

    @Autowired
    RabbitTemplate template;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Result result;

    @Autowired
    MinioUtil minioUtil;

    /**
     * 广播
     *
     *
     * */
    public void announce(GeneralMessage message) throws JsonProcessingException {
        template.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC,RabbitMQConfig.KEY_ANNOUNCE,
                objectMapper.writeValueAsString(message));
    }

    /**
     * 交货消息
     *
     * */
    public void delivery(GoodAccountBO bo) throws JsonProcessingException {
        DeliveryMessage message=new DeliveryMessage(bo);
        template.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC,RabbitMQConfig.KEY_DELIVERY,
                objectMapper.writeValueAsString(message));
    }

    /**
     * 一般通知
     *
     * */
    public void general(GeneralMessage message) throws JsonProcessingException {
        template.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC,RabbitMQConfig.KEY_GENERAL,
                objectMapper.writeValueAsString(message));
    }

    /**
     * 用户聊天
     *
     * */
    public void chat(@Payload ChatMessage message) throws JsonProcessingException {
        if (message instanceof ChatTextMessage textMessage){
            template.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC,RabbitMQConfig.KEY_CHAT,
                    objectMapper.writeValueAsString(textMessage));
        }
        if (message instanceof ChatPicMessage picMessage){
            template.convertAndSend(RabbitMQConfig.EXCHANGE_TOPIC,RabbitMQConfig.KEY_CHAT,
                    objectMapper.writeValueAsString(picMessage));
        }

        //todo 用另一个线程储存聊天记录
    }

    @MessageMapping("/topic/aaa")
    @SendTo("/topic/bbb")
    public String aaa(@Payload ChatMessage message) {
        return message.getUserName()+" hello! it's aaa here";
    }


    /**
     * 用户聊天时发送图片
     *
     * */
    @PostMapping("/chats/imgs/{user_id}/{user_id}")
    public String postChatImg(@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                              MultipartFile picture,Long time,
                              @PathVariable("user_id") int goodId,@PathVariable("user_id") int userId)
            throws JsonProcessingException {
        // 验参
        ParameterWrongBean bean =new ParameterWrongBean();
        if (goodId==0){
            bean.addParameters("good_id", goodId+"");
        }
        if (userId==0){
            bean.addParameters("user_id", userId+"");
        }
        if (picture == null){
            bean.addParameters("picture","图片");
        }
        if (time == null || time<=0){
            bean.addParameters("time",time+"");
        }
        if (! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (userId != details.getUserId()){
                throw new ForbiddenException();
            }
            //todo 在另一个线程储存图片
            String url=minioUtil.saveChatPicture(picture,time,goodId,userId);

            //todo 在另一个线程储存聊天记录

            // 推送消息
            ChatPicMessage message=new ChatPicMessage(authentication.getName(),time,url);
            chat(message);

            return result.gainPostSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

}
