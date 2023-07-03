package com.gseek.gs.websocket.message.chat;

import com.gseek.gs.websocket.message.BaseMessage;

/**
 * @author Phak
 * @since 2023/6/30-12:46
 */
public class ChatMessage extends BaseMessage {

    public ChatMessage(Integer fromUserId, Integer toUserId, Integer goodId,
                       String fromUserName, String type, String message, Long time) {
        super(fromUserId, toUserId, goodId, fromUserName, type, message, time);
    }
}
