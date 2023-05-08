package com.gseek.gs.websocket.message.chat;


/**
 * 封装文本消息体格式。
 *
 * @author Phak
 * @since 2023/5/8-22:16
 */
public class ChatTextMessage extends ChatMessage{

    /**
     * 消息文本
     * */
    private String text;

    public ChatTextMessage(String userName, long time, String text) {
        super(userName, time, ChatMessageType.TEXT.name());
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
