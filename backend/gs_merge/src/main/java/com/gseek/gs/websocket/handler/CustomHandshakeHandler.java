package com.gseek.gs.websocket.handler;

import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            @NotNull ServerHttpRequest request, @NotNull WebSocketHandler wsHandler,
            @NotNull Map<String, Object> attributes
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails)authentication.getDetails();
        return () -> details.getUserId()+"";
    }


}
