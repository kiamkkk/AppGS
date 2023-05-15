package com.gseek.gs.config;

import com.gseek.gs.websocket.handel.GetHeaderParamInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket配置.
 *
 * @author Phak
 * @since 2023/5/8-22:08
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    GetHeaderParamInterceptor getHeaderParamInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 使用RabbitMQ做为消息代理，替换默认的Simple Broker
        //定义了服务端接收地址的前缀，也即客户端给服务端发消息的地址前缀,@SendTo(XXX) 也可以重定向
        registry.setUserDestinationPrefix("/user"); //这是给sendToUser使用,前端订阅需要加上/user
        registry.setApplicationDestinationPrefixes("/app");
        // "STOMP broker relay"处理所有消息将消息发送到外部的消息代理
        registry.enableStompBrokerRelay("/exchange","/topic","/queue","/amq/queue")
                .setVirtualHost("GseekHost") //对应自己rabbitmq里的虚拟host
                .setRelayHost("localhost")
                .setClientLogin("root")
                .setClientPasscode("root")
                .setSystemLogin("root")
                .setSystemPasscode("root")
                .setSystemHeartbeatSendInterval(5000)
                .setSystemHeartbeatReceiveInterval(4000);

    }
    /**
     * 采用自定义拦截器，获取connect时候传递的参数
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(getHeaderParamInterceptor);
    }
}