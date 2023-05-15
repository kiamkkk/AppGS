package com.gseek.gs.websocket.message.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 封装图片信息体格式。
 *
 * @author Phak
 * @since 2023/5/8-22:18
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatPicMessage extends ChatMessage {

    /**
     * 图片url
     * */
    private String url;

    public ChatPicMessage(String userName, long time, String url) {
        super(userName, time, ChatMessageType.PICTURE.name());
        this.url = url;
    }

}
