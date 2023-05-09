package com.gseek.gs.websocket.message.chat;

/**
 * 封装图片信息体格式。
 *
 * @author Phak
 * @since 2023/5/8-22:18
 */
public class ChatPicMessage extends ChatMessage {

    /**
     * 图片url
     * */
    private String url;

    public ChatPicMessage(String userName, long time, String url) {
        super(userName, time, ChatMessageType.PICTURE.name());
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
