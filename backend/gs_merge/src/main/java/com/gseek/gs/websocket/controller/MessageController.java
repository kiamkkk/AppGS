package com.gseek.gs.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.dto.ChatBlockDTO;
import com.gseek.gs.pojo.dto.PostChatImgDTO;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.websocket.message.*;
import com.gseek.gs.websocket.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import static com.gseek.gs.websocket.message.BaseMessage.SYSTEM_GOOD_ID;

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
    @Autowired
    @Qualifier("adminServiceImpl")
    AdminService adminService;

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
     * 被加入黑名单通知
     *
     * */
    public void blacklist(BlacklistBO blacklistBO) {
        BlacklistNotice message=new BlacklistNotice(blacklistBO,objectMapper);
        messageService.sendMessage(message);
    }
    /**
     * 被移出
     * 黑名单通知
     *
     * */
    public void blacklistRemove(int toUserId){
        BlacklistNotice blacklistNotice=new BlacklistNotice("被移出黑名单",toUserId);
        messageService.sendMessage(blacklistNotice);
    }
    /**
     * 被加入申诉通知
     *
     * */
    public void appeal( AppealMessageBean appealMessageBean){
        AppealNoticeMessage appealNoticeMessage=new AppealNoticeMessage(appealMessageBean);
        messageService.sendMessage(appealNoticeMessage);
    }
    /**
     * 被移出黑名单通知
     *
     * */
    public void appealRemove(AppealMessageBean appealMessageBean){
        AppealNoticeMessage appealNoticeMessage=new AppealNoticeMessage("被移出黑名单",appealMessageBean);
        messageService.sendMessage(appealNoticeMessage);
    }

    /**
     * 用户聊天
     *
     * */
    @MessageMapping("/user/chat")
    public void chat(@Payload BaseMessage message) throws JsonProcessingException {
       messageService.sendMessage(message);
        // 用另一个线程储存聊天记录
        chatRecordService.insertMessage(message);
    }
    /**
     * 客服聊天
     *
     * */
    @MessageMapping("/user/admin/chat")
    public void adminChat(@Payload AdminMessage message) throws JsonProcessingException {
        int goodId=message.getGoodId();
        int fromUserId=message.getFromUserId();
        String identity=message.getFromUserName();
        //TODO 告诉前端有身份要求
        if(identity.equals("用户")){
            int[] adminId=chatRecordService.selectToUser(goodId,fromUserId);
            if(adminId[0]!=0){
                message.setToUserId(adminId[0]);
            }
            else{
                int newAdminId=adminService.selectRandomAdmin();
                message.setToUserId(newAdminId);
            }
        }
        if(identity.equals("管理员")){
            int[] toUserId=chatRecordService.selectToUser(goodId,fromUserId);
            message.setToUserId(toUserId[0]);
        }
        messageService.sendMessage(message);
        chatRecordService.insertMessage(message);
    }

    /**
     * 用户聊天时发送图片
     *
     * */
    @PostMapping("/chats/imgs/{good_id}/{user_id}")
    public String postChatImg(@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                              HttpServletRequest request,
                              @PathVariable("good_id") int goodId,@PathVariable("user_id") int userId)
            throws JsonProcessingException, com.gseek.gs.exce.ServerException, IllegalBlockSizeException, BadPaddingException {
        PostChatImgDTO dto=new PostChatImgDTO(request);
        dto.perService();

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (userId != details.getUserId()){
                throw new ForbiddenException();
            }
            //todo 在另一个线程储存图片
            String url=minioUtil.saveChatPicture(dto.getPicture(),dto.getTime(),goodId,userId);

            //todo 在另一个线程储存聊天记录

            // 推送消息
            ChatPicMessage message=new ChatPicMessage(userId, dto.getToUserId(), goodId,
                    authentication.getName(), url, dto.getTime());
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
        return chatRecordService.getChatRecords(goodId,userId);
    }
    @GetMapping("/chats/admin/records/{goodId}/{userId}")
    public String getAdminChatRecords(@PathVariable("goodId") int goodId,@PathVariable("userId") int userId) throws JsonProcessingException {
        //TODO 提醒前端goodId=-1
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
