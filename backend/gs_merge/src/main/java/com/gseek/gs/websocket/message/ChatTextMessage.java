package com.gseek.gs.websocket.message;


/**
 * @author Phak
 * @since 2023/5/16-18:45
 */
public class ChatTextMessage extends BaseMessage{
    public ChatTextMessage(Integer fromUserId, Integer toUserId, Integer goodId,
                           String fromUserName, String message, Long time) {
        super(fromUserId, toUserId, goodId, fromUserName,
                MessageType.CHAT_TEXT.name(), message, time);
    }

    public ChatTextMessage(BaseMessage bm) {
        super(SYSTEM_FROM_USER_ID, bm.getFromUserId(), bm.getGoodId(),
                SYSTEM_FROM_USER_NAME, bm.getType(), "该消息已经被对方拒收", bm.getTime());
    }
}
