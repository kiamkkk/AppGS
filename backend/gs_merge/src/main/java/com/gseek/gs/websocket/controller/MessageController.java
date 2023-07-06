package com.gseek.gs.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.controller.Controller;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.exce.business.websocket.chat.WebSocketException;
import com.gseek.gs.exce.business.websocket.chat.WrongSubscribeException;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.bean.BlacklistBean;
import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.dto.ChatBlockDTO;
import com.gseek.gs.pojo.dto.PostChatImgDTO;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.websocket.message.*;
import com.gseek.gs.websocket.message.chat.ChatMessage;
import com.gseek.gs.websocket.message.chat.ChatPicMessage;
import com.gseek.gs.websocket.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/8-23:11
 */
@Slf4j
@RestController
@ServerEndpoint("/websocket")
public class MessageController implements Controller {

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
    @Lazy
    AdminService adminService;

    @MessageExceptionHandler(WebSocketException.class)
    public void exp(Exception e) {
        // todo 异常处理
        if(e instanceof WebSocketException ){
            if (e instanceof WrongSubscribeException wse){
                messageService.disconnect(wse);
            }
        }
    }
    /**
     * 接收聊天消息
     * */
    @MessageMapping("/user/queue/chat")
    public void receiveChat(ChatMessage message){
        messageService.sendMessage(message);
    }

    /**
     * 广播
     * */
    public void announce(AnnounceMessage message) {
        messageService.sendMessage(message);
    }

    /**
     * 交货消息
     * */
    public void delivery(long time, GoodAccountBO bo) {
        NoticeMessage message=new NoticeMessage(time,bo,objectMapper);
        messageService.sendMessage(message);
    }
    /**
     * 一般通知
     * */
    public void general(NoticeMessage message) {
        messageService.sendMessage(message);
    }
    /**
     * 被加入黑名单通知
     *
     * */
    public void blacklist(BlacklistBean blacklistBO) {
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
     * Content-Type:multipart/form-data
     *
     * @deprecated 待改造
     * */

    @PostMapping("/chats/imgs/{good_id}/{user_id}")
    public String postChatImg(@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                              HttpServletRequest request,
                              @PathVariable("good_id") int goodId,@PathVariable("user_id") int userId)
            throws JsonProcessingException, ServerException, IllegalBlockSizeException, BadPaddingException {
        CustomWebAuthenticationDetails details = perService(authentication);
        PostChatImgDTO dto=new PostChatImgDTO(request);
        dto.perService();

        if (userId != details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        // 储存图片
        String url=minioUtil.saveChatPicture(dto.getPicture(),dto.getTime(),goodId,userId);
        // 储存聊天记录
        chatRecordService.insertMessage(new ChatPicMessage(
                        dto, goodId, userId, authentication.getName(),url
                )
        );
        // 推送消息
        ChatPicMessage message=new ChatPicMessage(userId, dto.getToUserId(), goodId,
                authentication.getName(), url, dto.getTime());
        //todo 改回来
        messageService.sendMessage(message);

        return result.gainPostSuccess();
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
    public String getChatBlock(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                               ChatBlockDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {

        CustomWebAuthenticationDetails details = perService(authentication);
        dto.perService();

        if (dto.getFromUserId() != details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        // 拉黑
        messageService.blockOrUnblock(dto);
        return result.gainPatchSuccess();

    }
}
