package com.gseek.gs.websocket.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Phak
 * @since 2023/5/16-22:14
 */
public enum MessageType {
    /**
     * 聊天文本消息
     * */
    CHAT_TEXT,
    /**
     * 聊天图片消息
     * */
    CHAT_PIC,
    /**
     * 公告消息
     * */
    ANNOUNCE,
    /**
     * 通知消息
     * */
    NOTICE,
    /**
     * 交货消息
     * */
    DELIVERY,
    /**
     * 被加入黑名单通知
     * */
    BLACKLIST,
    /**
     * 被移出黑名单通知
     * */
    BLACKLIST_REMOVE,
    /**
     * 被申诉通知
     * */
    APPEAL;

    private static Map<String,MessageType> typeMap=new HashMap<>(16);

    static {
        for (MessageType type:MessageType.values()){
            typeMap.put(type.name(), type);
        }
    }

    public static MessageType getTypeByName(String name){
        return typeMap.get(name);
    }
}
