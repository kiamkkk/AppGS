package com.gseek.gs.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.dto.ChatBlockDTO;
import com.gseek.gs.websocket.message.BaseMessage;
import com.gseek.gs.websocket.message.ChatTextMessage;
import com.gseek.gs.websocket.message.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author Phak
 * @since 2023/5/16-13:42
 */
@Service
@Slf4j
public class MessageService {

    @Autowired
    private SimpMessageSendingOperations operations;
    @Autowired
    ObjectMapper objectMapper;
    /**
     * 用户id ： 拉黑该用户的用户id
     * */
    public static Map<Integer, Set<Integer>> blockMap=new HashMap<>(16);

    public void receiveMsg(String msg) throws JsonProcessingException {
        BaseMessage bm=objectMapper.readValue(msg, BaseMessage.class);
        sendMessage(bm,msg);
    }

    public void sendMessage(BaseMessage bm,String msg){
        MessageType type=MessageType.getTypeByName(bm.getType());
        switch (type) {
            case ANNOUNCE -> {
                operations.convertAndSend("/topic/announcement", bm);
            }
            case NOTICE -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/remind/general", bm);
            }
            case DELIVERY -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/remind/delivery", bm);
            }
            case CHAT_PIC,CHAT_TEXT-> {
                // 遍历拉黑该用户的set，确定该消息应不应该发出
                Set<Integer> blocks=blockMap.get(bm.getFromUserId());
                int toUserId=bm.getToUserId();
                boolean flag=false;
                for (int userId:blocks){
                    if (userId==toUserId){
                        ChatTextMessage cm=new ChatTextMessage(bm);
                        operations.convertAndSendToUser(bm.getFromUserId() + "", "/chat", cm);
                        flag=true;
                        return;
                    }
                }

                if (flag){
                    return;
                }
                operations.convertAndSendToUser(bm.getToUserId() + "", "/chat", bm);
            }
            default -> {
                log.error("无法从json中识别消息类别，消息内容为:\n{}", msg);
            }
        }
    }

    public void sendMessage(BaseMessage bm){
        MessageType type=MessageType.getTypeByName(bm.getType());
        switch (type) {
            case ANNOUNCE -> {
                operations.convertAndSend("/topic/announcement", bm);
            }
            case NOTICE -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/remind/general", bm);
            }
            case DELIVERY -> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/remind/delivery", bm);
            }
            case CHAT_PIC,CHAT_TEXT-> {
                operations.convertAndSendToUser(bm.getToUserId() + "", "/chat", bm);
            }
            default -> {
                log.error("无法识别消息类别，消息内容为:\n{}", bm);
            }
        }
    }

    public void blockOrUnblock(ChatBlockDTO dto){
        if (blockMap.containsKey(dto.getToUserId())){
            blockMap.put(dto.getFromUserId(), new HashSet<>(16));
        }
        Set<Integer> blocks=blockMap.get(dto.getToUserId());
        if (dto.getBlock()){
            blocks.add(dto.getFromUserId());
        }else {
            blocks.remove(dto.getFromUserId());
        }
    }
}
