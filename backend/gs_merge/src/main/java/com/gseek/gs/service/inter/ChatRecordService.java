package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.websocket.message.BaseMessage;

/**
 * @author Phak
 * @since 2023/5/17-9:49
 */
public interface ChatRecordService {
    //todo 补充注释

    /**
     *
     *
     * */
    String getChatRecords(int goodId,int userId) throws JsonProcessingException;

    void insertMessage(BaseMessage message);
}
