package com.gseek.gs.websocket.message.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 消息体格式。
 *
 * @author Phak
 * @since 2023/5/8-22:19
 */
public abstract class ChatMessage {

    /**
     * 发送用户名
     * */
    protected String userName;
    /**
     * 发送时间
     * */
    protected long time;
    /**
     * 信息类型
     * */
    protected String messageType;

    public ChatMessage() {
    }

    public ChatMessage(String userName, long time, String messageType) {
        this.userName = userName;
        this.time = time;
        this.messageType = messageType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
