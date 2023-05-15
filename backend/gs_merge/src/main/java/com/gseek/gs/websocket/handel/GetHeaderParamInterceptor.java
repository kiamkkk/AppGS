package com.gseek.gs.websocket.handel;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Phak
 * @since 2023/5/15-17:13
 */
@Component
public class GetHeaderParamInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                //取出客户端携带的参数
                Object userId = ((Map) raw).get("userId");
                System.out.println(userId);
                if (userId instanceof LinkedList) {
                    // 设置当前访问的认证用户
                    accessor.setUser(new UserPrincipal(((LinkedList) userId).get(0).toString()));
                }
            }
        }
        return message;
    }
}
