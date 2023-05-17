package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.dao.ChatRecordMapper;
import com.gseek.gs.pojo.data.ChatDO;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.websocket.message.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/17-9:49
 */
@Service("chatRecordServiceImpl")
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    ChatRecordMapper chatRecordMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String getChatRecords(int goodId, int userId)
            throws JsonProcessingException {
        List<ChatDO> chatDOS=chatRecordMapper.selectChat(goodId, userId);
        return objectMapper.writeValueAsString(chatDOS);
    }

    @Override
    public void insertMessage(BaseMessage message) {
        chatRecordMapper.insertChat(new ChatDO(message));
    }
}
