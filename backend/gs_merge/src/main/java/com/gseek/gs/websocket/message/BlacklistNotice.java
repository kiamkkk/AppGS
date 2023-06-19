package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.bean.BlacklistBean;

/**
 * @author: Isabella
 * @create: 2023-05-24 22:39
 **/
public class BlacklistNotice extends BlacklistMessageBase{
    public BlacklistNotice(BlacklistBean bo, ObjectMapper objectMapper){
        super(SYSTEM_FROM_USER_ID,bo.getRespondentId(), bo.getAppealReason(), MessageType.BLACKLIST.name(), bo.toMessage(objectMapper));
    }
    public BlacklistNotice(String message,int toUserId){
        super(SYSTEM_FROM_USER_ID,toUserId, "因撤销举报", MessageType.BLACKLIST_REMOVE.name(), message);
    }
}
