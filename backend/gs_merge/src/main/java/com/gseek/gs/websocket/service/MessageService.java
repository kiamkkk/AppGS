package com.gseek.gs.websocket.service;

import com.gseek.gs.exce.business.websocket.chat.WebSocketException;
import com.gseek.gs.exce.business.websocket.chat.WrongSubscribeException;
import com.gseek.gs.pojo.dto.ChatBlockDTO;

import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.websocket.message.*;
import com.gseek.gs.websocket.message.chat.ChatMessage;
import com.gseek.gs.websocket.message.chat.ChatTextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.gseek.gs.websocket.message.BaseMessage.SYSTEM_GOOD_ID;


/**
 * @author Phak
 * @since 2023/5/16-13:42
 */
@Service("messageService")
@Slf4j
public class MessageService {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private RedisTemplate<String, BaseMessage> offlineTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AdminService adminService;
    private StompEncoder stompEncoder = new StompEncoder();
    @Autowired
    @Qualifier("chatRecordServiceImpl")
    private ChatRecordService chatRecordService;


    @Autowired
    private SimpUserRegistry simpUserRegistry;

    /**
     * sessionId:WebSocketSession
     * */
    private static Map<String, WebSocketSession> sessionIdToSession = new ConcurrentHashMap<>();

    /**
     * 用户id ： 拉黑该用户的用户id
     * */
    public static Map<Integer, Set<Integer>> blockMap=new HashMap<>(16);

    public static void putSession(WebSocketSession session){
        // 记录sessionId与session关系
        sessionIdToSession.put(session.getId(), session);
    }

    /**
     * 关闭sessionId对应的session
     * */
    public static void closeSession(String sessionId) {
        WebSocketSession session = sessionIdToSession.get(sessionId);
        if ( session.isOpen() ){
            try {
                session.close();
            } catch (IOException e) {
                // todo StompSubProtocolHandler#sendErrorMessage中直接忽略掉了，所以我也学他这么做
                // todo 但抛出IOException就说明关闭掉了？
            }finally {
                sessionIdToSession.remove(sessionId);
            }
        }
    }
    /**
     * 发生异常时断开连接
     * 向用户推送error帧，并关闭连接
     * */
    public void disconnect(WrongSubscribeException wse){
        sendError(wse);
        closeSession(wse.getSessionId());
    }

    /**
     * 向用户推送error帧
     * */
    private void sendError(WebSocketException wse){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage(wse.getMessage());

        WebSocketSession session = sessionIdToSession.get(wse.getSessionId());
        byte[] bytes = this.stompEncoder.encode(headerAccessor.getMessageHeaders(), wse.toMessagePayload());
        try {
            session.sendMessage(new TextMessage(bytes));
        }
        catch (Throwable ex) {
            log.debug("Error 帧发送失败:", ex);
        }

    }

    /**
     * 发送公告
     * */
    public void sendMessage(AnnounceMessage message){
        rabbitTemplate.convertAndSend("amq.fanout",null,message);
    }

    /**
     * 对特定用户发送通知
     * */
    public void sendMessage(NoticeMessage message){
        // 不在线，储存消息
        if (! checkOnline(message.getToUserId())){
            saveOfflineMessage(message.getToUserId(), message);
            return;
        }

        if (message.getType().equals(MessageType.DELIVERY.toString())){
            template.convertAndSendToUser(message.getToUserId()+"", "/queue/delivery", message);
        }
        template.convertAndSendToUser(message.getToUserId()+"", "/queue/notice", message);

    }

    /**
     * 对特定用户发送聊天消息
     * */
    public void sendMessage(ChatMessage message){
        // 不在线，储存消息
        if (! checkOnline(message.getToUserId())){
            saveOfflineMessage(message.getToUserId(), message);
            return;
        }

        if (! checkBlock(message)){
            message = new ChatTextMessage(message);
        }
        template.convertAndSendToUser(message.getToUserId()+"", "/queue/chat", message);
        // 保存聊天记录
        chatRecordService.insertMessage(message);
    }


    /**
     * 黑名单的消息提醒
     * */
    public void sendMessage(BlacklistMessageBase message){
        // 不在线，储存消息
        if (! checkOnline(message.getToUserId())){
            saveOfflineMessage(message.getToUserId(), message);
            return;
        }

        if (message.getType().equals(MessageType.BLACKLIST.toString())||message.getType().equals(MessageType.BLACKLIST_REMOVE.toString())){
            template.convertAndSendToUser(message.getToUserId()+"", "/queue/blacklist", message);
        }
        template.convertAndSendToUser(message.getToUserId()+"", "/queue/notice", message);
    }

    /**
     * 申诉的消息提醒
     * */
    public void sendMessage(AppealMessageBase message){
        // 不在线，储存消息
        if (! checkOnline(message.getToUserId())){
            saveOfflineMessage(message.getToUserId(), message);
            return;
        }

        if (message.getType().equals(MessageType.APPEAL.toString())){
            template.convertAndSendToUser(message.getToUserId()+"", "/queue/appeal", message);
        }
        template.convertAndSendToUser(message.getToUserId()+"", "/queue/notice", message);
    }
    /**
     * 客服与用户的聊天
     * */
    public void sendMessage(AdminMessage message){
        int goodId=SYSTEM_GOOD_ID;
        int fromUserId=message.getFromUserId();
        String identity=message.getFromUserName();
        // 不在线，储存消息
        if (! checkOnline(message.getToUserId())){
            saveOfflineMessage(message.getToUserId(), message);
            return;
        }
//        身份为用户
        if(identity.equals("USER")){
//            寻找上一个与该用户聊天的客服
            int[] adminId=chatRecordService.selectToUser(goodId,fromUserId);
            if(adminId[0]!=0){
                message.setToUserId(adminId[0]);
            }
            else{
//                该用户之前没和管理员聊天过
                int newAdminId=adminService.selectRandomAdmin();
                message.setToUserId(newAdminId);
            }
            template.convertAndSendToUser("ADMIN"+message.getToUserId()+"", "/queue/admin_chat", message);
        }
//        身份为管理员
        if(identity.equals("ADMIN")){
            template.convertAndSendToUser(message.getToUserId()+"", "/queue/admin_chat", message);
        }
        // 保存聊天记录
        chatRecordService.insertMessage(message);
    }


    public void blockOrUnblock(ChatBlockDTO dto){
        if (! blockMap.containsKey(dto.getToUserId())){
            blockMap.put(dto.getFromUserId(), new CopyOnWriteArraySet<>());
        }
        Set<Integer> blocks=blockMap.get(dto.getToUserId());
        if (dto.getBlock()){
            blocks.add(dto.getFromUserId());
        }else {
            blocks.remove(dto.getFromUserId());
        }
    }

    /**
     * 没被拉黑返回true
     * */
    private boolean checkBlock(ChatMessage message){
        int userId = message.getFromUserId();
        // 确认用户有无被拉黑过
        if (! blockMap.containsKey(userId)){
            return true;
        }
        for(Map.Entry<Integer,Set<Integer>> entry: blockMap.entrySet()){
            if (entry.getKey() != userId){
                continue;
            }
            return ! entry.getValue().contains(message.getToUserId());
        }
        return true;
    }
    /**
     * 在线返回true
     * */
    private boolean checkOnline(int userId){
        return simpUserRegistry.getUser(userId+"") != null;
    }

    private void saveOfflineMessage(int userId, BaseMessage message){
        offlineTemplate.opsForSet().add(userId+"", message);
    }
}
