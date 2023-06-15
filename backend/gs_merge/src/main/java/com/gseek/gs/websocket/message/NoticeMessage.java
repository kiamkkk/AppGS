package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.business.GoodAccountBO;

/**
 * @author Phak
 * @since 2023/5/16-18:46
 */
public class NoticeMessage extends BaseMessage{


    /**
     * 一般通知
     * */
    public NoticeMessage(String message, Long time, int toUserId) {
        super(SYSTEM_FROM_USER_ID, toUserId, SYSTEM_GOOD_ID, SYSTEM_FROM_USER_NAME,
                MessageType.NOTICE.name(), message, time);
    }
    /**
     * 交货消息
     * */
    public NoticeMessage(Long time, GoodAccountBO bo, ObjectMapper objectMapper) {
        super(SYSTEM_FROM_USER_ID, bo.getBuyerId(),SYSTEM_GOOD_ID,SYSTEM_FROM_USER_NAME,
                MessageType.DELIVERY.name(), bo.toMessage(objectMapper), time);
    }



}
