package com.gseek.gs.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Phak
 * @since 2023/5/8-23:02
 */
@Slf4j
@Component
public class WebSocketDecoratorFactory  implements WebSocketHandlerDecoratorFactory {
// todo 用户离线后怎么在下一次登录时推送给用户？？？
    private static ConcurrentHashMap<String, WebSocketSession> manager = new ConcurrentHashMap<String, WebSocketSession>();
    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {

        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("新用户接入");
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    log.info("用户id{}接入", principal.getName());
                    // 身份校验成功，缓存socket连接
                    add(principal.getName(), session);
                }


                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("用户退出");
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    // 身份校验成功，移除socket连接
                    remove(principal.getName());
                }
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }

    private void add(String key, WebSocketSession webSocketSession) {
        log.info("新添加webSocket连接 {} ", key);
        manager.put(key, webSocketSession);
    }

    private void remove(String key) {
        log.info("移除webSocket连接 {} ", key);
        manager.remove(key);
    }

    public static WebSocketSession get(String key) {
        log.info("获取webSocket连接 {}", key);
        return manager.get(key);
    }

}
