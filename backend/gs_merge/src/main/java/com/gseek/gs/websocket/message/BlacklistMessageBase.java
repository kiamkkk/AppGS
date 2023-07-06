package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @author: Isabella
 * @create: 2023-05-24 22:36
 **/
@Setter
@Getter
@AllArgsConstructor
public class BlacklistMessageBase extends BaseMessage{
    @JsonIgnore
    public final static Integer SYSTEM_FROM_USER_ID =-1;
    @JsonIgnore
    public final static Integer SYSTEM_TO_USER_ID =-1;
    @JsonIgnore
    public final static String SYSTEM_FROM_USER_NAME ="System";
    protected Integer fromUserId;

    protected Integer toUserId;
    protected String reason;
    protected String type;
    protected String message;
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("fromUserId").append(fromUserId).append("\n")
                .append("toUserId").append(toUserId).append("\n")
                .append("reason").append(reason).append("\n")
                .append("type").append(type).append("\n")
                .append("message").append(message).append("\n");
        return sb.toString();

    }
}
