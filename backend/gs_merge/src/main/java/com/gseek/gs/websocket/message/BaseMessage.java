package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/16-18:41
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {
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
    protected Integer goodId;
    protected String fromUserName;
    protected String type;
    protected String message;
    protected Long time;

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("fromUserId:").append(fromUserId)
          .append("toUserId").append(toUserId)
          .append("goodId").append(goodId)
          .append("fromUserName").append(fromUserName)
          .append("type").append(type)
          .append("message").append(message)
          .append("time").append(time);
        return sb.toString();

    }
}
