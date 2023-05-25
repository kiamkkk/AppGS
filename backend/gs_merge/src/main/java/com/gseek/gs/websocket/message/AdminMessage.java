package com.gseek.gs.websocket.message;

/**
 * @author: Isabella
 * @create: 2023-05-26 00:19
 **/
public class AdminMessage extends BaseMessage{

    public AdminMessage(int toUserId, int fromUserId,String message,Long time,String identity) {
        super(fromUserId, toUserId, SYSTEM_GOOD_ID, identity+fromUserId,
                MessageType.ADMIN.name(), message, time);
    }
}
