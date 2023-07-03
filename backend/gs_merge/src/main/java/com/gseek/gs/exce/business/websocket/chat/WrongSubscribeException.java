package com.gseek.gs.exce.business.websocket.chat;

import org.springframework.messaging.Message;

/**
 * @author Phak
 * @since 2023/7/2-13:58
 */
public class WrongSubscribeException extends WebSocketException{

    public WrongSubscribeException(Message sourceMessage, String toUserId, String destination, String sessionId) {
        super("无权订阅", sourceMessage, toUserId, destination, sessionId);
    }

}
