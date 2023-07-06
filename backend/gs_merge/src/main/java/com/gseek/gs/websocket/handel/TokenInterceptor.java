package com.gseek.gs.websocket.handel;

import com.gseek.gs.common.Token;
import com.gseek.gs.common.TokenGrade;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TokenInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                //取出客户端携带的参数
                Object header = ((Map) raw).get("Authentication");
                log.debug("header: {}",header);
                if (header instanceof LinkedList) {
                    // 设置当前访问的认证用户
                    String userId= TokenUtil.extractClaim((String) ((LinkedList<?>) header).get(0), Claims::getSubject);
//                    获取身份信息
                    String rawToken = ((Map) raw).get("Authorization").toString();
                    String cookedToken = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
                    Token token=new Token(cookedToken);
                    TokenGrade grade=token.getGrade();
//                    用户为管理员加前缀
                    if(grade.equals(TokenGrade.ADMIN)){
                        accessor.setUser( new UserPrincipal("ADMIN"+userId) );
                    }
                    accessor.setUser( new UserPrincipal(userId) );
                }
            }
        }
        return message;
    }
}
