package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.dao.ChatRecordMapper;
import com.gseek.gs.pojo.data.ChatDO;
import com.gseek.gs.service.inter.ChatRecordService;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.service.inter.ScheduledService;
import com.gseek.gs.websocket.message.BaseMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/17-9:49
 */
@Service("chatRecordServiceImpl")
public class ChatRecordServiceImpl implements ChatRecordService, ScheduledService {
//todo 补充注释
    @Autowired
    ChatRecordMapper chatRecordMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;

    @Override
    public String getChatRecords(int goodId, int userId)
            throws JsonProcessingException {
        List<ChatDO> chatDOS=chatRecordMapper.selectChat(goodId, userId);
        return objectMapper.writeValueAsString(chatDOS);
    }

    @Async("async")
    @Override
    public void insertMessage(BaseMessage message) {
        redisService.saveChatRecode(message);
    }

    /**
     * 将redis中的聊天记录储存在mysql里.
     * 每30秒同步一次
     * */
    @Async("async")
    @Scheduled(cron="*/30 * * * * ?")
    @Override
    public void redisToMysql(){
        List<ChatDO> chatDOs=redisService.getChatRecodes();
        if (chatDOs.size()>0){
            chatRecordMapper.insertChat(chatDOs);
        }
    }
    public int[] selectToUser(@Param("goodId") int goodId, @Param("fromUserId") int fromUserId){
        return chatRecordMapper.selectToUser(goodId,fromUserId);
    }

}
