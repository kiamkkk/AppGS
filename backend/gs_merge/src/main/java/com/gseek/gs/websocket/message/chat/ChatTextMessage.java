package com.gseek.gs.websocket.message.chat;


import com.gseek.gs.websocket.message.MessageType;

/**
 * @author Phak
 * @since 2023/5/16-18:45
 */
public class ChatTextMessage extends ChatMessage {
    public ChatTextMessage(Integer fromUserId, Integer toUserId, Integer goodId,
                           String fromUserName, String message, Long time) {
        super(fromUserId, toUserId, goodId, fromUserName,
                MessageType.CHAT_TEXT.name(), message, time);
    }

    /**
     * 消息被拒收
     * */
    public ChatTextMessage(ChatMessage cm) {
        super(cm.getToUserId(), cm.getFromUserId(), cm.getGoodId(),
                SYSTEM_FROM_USER_NAME, MessageType.BLOCK.name(), "该消息已经被对方拒收", cm.getTime());
    }
}
