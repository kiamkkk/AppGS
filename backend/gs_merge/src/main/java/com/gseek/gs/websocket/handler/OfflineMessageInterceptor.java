package com.gseek.gs.websocket.handler;

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

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        if (sent){
            StompHeaderAccessor accessor =
                    SimpMessageHeaderAccessor.getAccessor(message.getHeaders(), StompHeaderAccessor.class);
            if (accessor.getCommand() == StompCommand.CONNECT){
                userOnline(accessor.getUser().getName());
            }
        }
    }

    private void userOnline(String userId){
        Set<BaseMessage> messages = offlineTemplate.opsForSet().members(userId);
        if (messages==null || messages.isEmpty()){
            return;
        }

        for (BaseMessage message:messages){
            MessageType type=MessageType.getTypeByName(message.getType());
            if ( type == MessageType.CHAT_TEXT || type == MessageType.CHAT_PIC ){
                template.convertAndSendToUser(userId, CHAT_DESTINATION, message);
            }else if ( type == MessageType.NOTICE ){
                template.convertAndSendToUser(userId, NOTICE_DESTINATION, message);
            }else {
                log.debug("消息类型为{}的离线消息未发出:\n{}", type, message);
            }
        }

        // 删除redis中所有离线消息
        offlineTemplate.delete(userId);
    }
}
