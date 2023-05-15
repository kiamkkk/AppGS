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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    /**
     * 发送用户id
     * */
//    protected int fromUserId;
    /**
     * 接收用户id
     * */
//    protected int toUserId;
    /**
     * 在该商品id的商品下进行的聊天
     * */
//    protected int billId;
}
