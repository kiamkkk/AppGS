package com.gseek.gs.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.dto.ChatBlockDTO;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.websocket.message.AnnounceMessage;
import com.gseek.gs.websocket.message.BaseMessage;
import com.gseek.gs.websocket.message.ChatPicMessage;
import com.gseek.gs.websocket.message.NoticeMessage;
import com.gseek.gs.websocket.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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

    @Autowired
    MessageService messageService;
    @Autowired
    @Qualifier("chatRecordServiceImpl")
    ChatRecordService chatRecordService;

    /**
     * 广播
     *
     *
     * */
    public void announce(AnnounceMessage message) {
        messageService.sendMessage(message);
    }

    /**
     * 交货消息
     *
     * */
    public void delivery(long time, GoodAccountBO bo) {
        NoticeMessage message=new NoticeMessage(time,bo,objectMapper);
        messageService.sendMessage(message);
    }

    /**
     * 一般通知
     *
     * */
    public void general(NoticeMessage message) {
        messageService.sendMessage(message);
    }

    /**
     * 用户聊天
     *
     * */
    @MessageMapping("/user/chat")
    public void chat(@Payload BaseMessage message) {
       messageService.sendMessage(message);
        //todo 用另一个线程储存聊天记录
        chatRecordService.insertMessage(message);
    }

    /**
     * 用户聊天时发送图片
     *
     * */
    @PostMapping("/chats/imgs/{good_id}/{user_id}")
    public String postChatImg(@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                              MultipartFile picture,Long time,int toUserId,
                              @PathVariable("good_id") int goodId,@PathVariable("user_id") int userId)
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
            ChatPicMessage message=new ChatPicMessage(userId, toUserId, goodId, authentication.getName(), url, time);
            chat(message);

            return result.gainPostSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @GetMapping("/chats/records/{good_id}/{user_id}")
    public String getChatRecords(@PathVariable("good_id") int goodId,@PathVariable("user_id") int userId)
            throws JsonProcessingException {
        // todo 获取聊天记录
        return chatRecordService.getChatRecords(goodId,userId);
    }

    @PatchMapping("/chats/block")
    public String getChatBlock(@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                               ChatBlockDTO dto)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {

        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (dto.getFromUserId() != details.getUserId()){
                throw new ForbiddenException();
            }
            // 拉黑
            messageService.blockOrUnblock(dto);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }


}
