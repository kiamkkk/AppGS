package com.gseek.gs.websocket;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author Phak
 * @since 2023/5/8-23:06
 */
@Component
@Slf4j
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {
            HttpServletRequest httpRequest = servletServerHttpRequest.getServletRequest();
            final String token = httpRequest.getHeader("Authentication");
            if (token.isBlank()) {
                return null;
            }
            return new Principal() {
                @Override
                public String getName() {
                    return token;
                }
            };
        }
        return null;
    }
}
