package com.gseek.gs.websocket.message;


/**
 * @author Phak
 * @since 2023/5/16-18:46
 */
public class AnnounceMessage extends BaseMessage {

    public AnnounceMessage(String message, Long time) {
        super(SYSTEM_FROM_USER_ID, SYSTEM_TO_USER_ID, SYSTEM_GOOD_ID, SYSTEM_FROM_USER_NAME,
                MessageType.ANNOUNCE.name(), message, time);
    }
}