package com.gseek.gs.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.websocket.message.BaseMessage;
import com.gseek.gs.websocket.message.NoticeMessage;
import com.gseek.gs.websocket.support.TestChannelInterceptor;
import com.gseek.gs.websocket.support.TestPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Phak
 * @since 2023/6/29-0:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WebsocketTest {
    @Autowired
    AbstractSubscribableChannel clientInboundChannel;
    @Autowired
    AbstractSubscribableChannel clientOutboundChannel;
    @Autowired
    AbstractSubscribableChannel brokerChannel;
    @Autowired
    ObjectMapper objectMapper;
    TestChannelInterceptor clientOutboundChannelInterceptor;
    TestChannelInterceptor brokerChannelInterceptor;
    @Before
    public void setUp(){
        this.brokerChannelInterceptor = new TestChannelInterceptor();
        this.clientOutboundChannelInterceptor = new TestChannelInterceptor();
        this.brokerChannel.addInterceptor(this.brokerChannelInterceptor);
        this.clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor);
    }

    @Test
    public void sendNotice() throws IOException, InterruptedException {
        NoticeMessage nm=new NoticeMessage("test", 1145141919810L, 3);
        byte[] payload = objectMapper.writeValueAsBytes(nm);

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.MESSAGE);
        headers.setDestination("/topic/announcement");
        headers.setSessionId("1");
        headers.setUser(new TestPrincipal("6"));
        headers.setSessionAttributes(new HashMap<>());
        Message<byte[]> message = MessageBuilder.createMessage(payload, headers.getMessageHeaders());

        this.brokerChannelInterceptor.setIncludedDestinations("/topic/announcement");
        this.clientInboundChannel.send(message);

        Message<?> positionUpdate = this.brokerChannelInterceptor.awaitMessage(2);
        assertNotNull(positionUpdate);
        log.error( (objectMapper.readValue((byte[])positionUpdate.getPayload(), BaseMessage.class)).toString() );

        StompHeaderAccessor positionUpdateHeaders = StompHeaderAccessor.wrap(positionUpdate);
        assertEquals("/topic/announcement", positionUpdateHeaders.getDestination());
    }
}
