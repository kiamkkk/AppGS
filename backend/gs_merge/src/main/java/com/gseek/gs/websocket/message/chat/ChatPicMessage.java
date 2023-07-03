package com.gseek.gs.websocket.message.chat;


import com.gseek.gs.pojo.dto.PostChatImgDTO;
import com.gseek.gs.websocket.message.MessageType;
import lombok.Getter;

/**
 * @author Phak
 * @since 2023/5/16-18:45
 */
@Getter
public class ChatPicMessage extends ChatMessage {
    public ChatPicMessage(Integer fromUserId, Integer toUserId, Integer goodId,
                          String fromUserName, String url, Long time) {
        super(fromUserId, toUserId, goodId,
              fromUserName, MessageType.CHAT_PIC.name(), url, time);
    }
    public ChatPicMessage(PostChatImgDTO dto, int goodId, int fromUserId, String fromUserName, String url) {
        super(fromUserId, dto.getToUserId(), goodId,
                fromUserName, MessageType.CHAT_PIC.name(), url, dto.getTime());
    }

}
