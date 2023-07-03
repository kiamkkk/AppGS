package com.gseek.gs.config;

import com.gseek.gs.websocket.handler.CustomHandshakeHandler;
import com.gseek.gs.websocket.handler.CustomWebSocketHandlerDecoratorFactory;
import com.gseek.gs.websocket.handler.OfflineMessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


/**
 * websocket配置.
 *
 * @author Phak
 * @since 2023/5/8-22:08
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer   {
    @Autowired
    RabbitMQConfig rabbitMQConfig;

    @Autowired
    OfflineMessageInterceptor omi;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new CustomHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setUserDestinationPrefix("/user");
        registry.enableStompBrokerRelay("/queue","/exchange")
                .setVirtualHost(rabbitMQConfig.virtualHost)
                .setClientLogin(rabbitMQConfig.username)
                .setClientPasscode(rabbitMQConfig.password)
                .setSystemLogin(rabbitMQConfig.username)
                .setSystemPasscode(rabbitMQConfig.password);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry){
        registry.addDecoratorFactory(new CustomWebSocketHandlerDecoratorFactory());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(omi);
    }
}