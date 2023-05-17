package com.gseek.gs.websocket.message;


import lombok.Getter;

/**
 * @author Phak
 * @since 2023/5/16-18:45
 */
@Getter
public class ChatPicMessage extends BaseMessage{
    public ChatPicMessage(Integer fromUserId, Integer toUserId, Integer goodId,
                          String fromUserName, String url, Long time) {
        super(fromUserId, toUserId, goodId,
              fromUserName, MessageType.CHAT_PIC.name(), url, time);
    }
}
