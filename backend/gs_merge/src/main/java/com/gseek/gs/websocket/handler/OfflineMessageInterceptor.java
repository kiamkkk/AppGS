package com.gseek.gs.websocket.handler;

import com.gseek.gs.util.StrUtil;
import com.gseek.gs.websocket.message.BaseMessage;
import com.gseek.gs.websocket.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 连接成功后推送离线消息
 *
 * @author Phak
 * @since 2023/7/3-15:44
 */
@Slf4j
@Component
public class OfflineMessageInterceptor implements ChannelInterceptor {

    private final String CHAT_DESTINATION = "/queue/chat";
    private final String NOTICE_DESTINATION = "/queue/notice";

    @Autowired
    @Lazy
    private RedisTemplate<String, BaseMessage> offlineTemplate;
    @Autowired
    @Lazy
    private SimpMessagingTemplate template;

/*    @Override
    public Message postReceive(Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor =
                SimpMessageHeaderAccessor.getAccessor(message.getHeaders(), StompHeaderAccessor.class);
        if (accessor.getCommand() == StompCommand.SUBSCRIBE){
            userOnline(accessor);
        }
        return message;
    }*/

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        if (sent){
            StompHeaderAccessor accessor =
                    SimpMessageHeaderAccessor.getAccessor(message.getHeaders(), StompHeaderAccessor.class);
            if (accessor.getCommand() == StompCommand.SUBSCRIBE){
                userOnline(accessor);
            }
        }
    }

    private void userOnline(StompHeaderAccessor accessor){
        boolean isChat = false;
        boolean isNotice = false;

        String userId = accessor.getUser().getName();
        String destination = accessor.getDestination();
        userId = userId == null ? "-2" : userId;
        destination = destination == null ? "" : destination;

        if (StrUtil.checkUserDestination(destination)){
            if (destination.endsWith("/chat")){
                isChat = true;
            }else if( destination.endsWith("/notice") || destination.endsWith("/delivery") ) {
                isNotice = true;
            }else {
                return;
            }
        }else {
            return;
        }

        Set<BaseMessage> messages = offlineTemplate.opsForSet().members(userId);
        if (messages==null || messages.isEmpty()){
            return;
        }

        for (BaseMessage message:messages){
            MessageType type=MessageType.getTypeByName(message.getType());

            if (isChat){
                if ( type == MessageType.CHAT_TEXT || type == MessageType.CHAT_PIC ){
                    template.convertAndSendToUser(userId, CHAT_DESTINATION, message);
                    // 删除redis中发出去的离线消息
                    offlineTemplate.opsForSet().remove(userId, message);
                    continue;
                }
            }

            if (isNotice){
                if ( type == MessageType.NOTICE || type == MessageType.DELIVERY ){
                    template.convertAndSendToUser(userId, NOTICE_DESTINATION, message);
                    // 删除redis中发出去的离线消息
                    offlineTemplate.opsForSet().remove(userId, message);
                    continue;
                }
            }

            log.warn("消息类型为{}的离线信息未发出: \n{}", type, message);
        }

    }
}
