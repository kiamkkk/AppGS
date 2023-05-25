package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;

/**
 * @author: Isabella
 * @create: 2023-05-24 22:39
 **/
public class BlacklistNotice extends BlacklistMessageBase{
    public BlacklistNotice(BlacklistBO bo, ObjectMapper objectMapper){
        super(SYSTEM_FROM_USER_ID,bo.getRespondent_id(), bo.getAppeal_reason(), MessageType.BLACKLIST.name(), bo.toMessage(objectMapper));
    }
    public BlacklistNotice(String message,int toUserId){
        super(SYSTEM_FROM_USER_ID,toUserId, "因撤销举报", MessageType.BLACKLIST_REMOVE.name(), message);
    }
}
