package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: Isabella
 * @create: 2023-05-24 23:07
 **/
@Setter
@Getter
@AllArgsConstructor
public class AppealMessageBase {
    @JsonIgnore
    public final static Integer SYSTEM_FROM_USER_ID =-1;
    @JsonIgnore
    public final static Integer SYSTEM_TO_USER_ID =-1;
    @JsonIgnore
    public final static Integer SYSTEM_GOOD_ID =-1;
    @JsonIgnore
    public final static String SYSTEM_FROM_USER_NAME ="System";
    protected Integer fromUserId;
    protected Integer toUserId;
    protected String goodName;
    protected Integer billId;
    protected String type;
    protected String message;
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("fromUserId").append(fromUserId)
                .append("toUserId").append(toUserId)
                .append("billId").append(billId)
                .append("goodName").append(goodName)
                .append("type").append(type)
                .append("message").append(message);
        return sb.toString();

    }

}
