package com.gseek.gs.websocket.handler;

import com.gseek.gs.websocket.service.MessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * 使WebSocketHandler在连接建立后记录session
 *
 * @author Phak
 * @since 2023/7/3-1:24
 */
public class CustomWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @NotNull
    @Override
    public WebSocketHandler decorate(@NotNull WebSocketHandler handler) {

        return new WebSocketHandlerDecorator(handler){

            @Override
            public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
                // 记录session
                MessageService.putSession(session);
                super.afterConnectionEstablished(session);
            }

        };

    }
}
