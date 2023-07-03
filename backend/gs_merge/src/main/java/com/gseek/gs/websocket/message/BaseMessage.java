package com.gseek.gs.websocket.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Phak
 * @since 2023/5/16-18:41
 */
@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
        sb.append("fromUserId:").append(fromUserId).append("\n")
          .append("toUserId:").append(toUserId).append("\n")
          .append("goodId:").append(goodId).append("\n")
          .append("fromUserName:").append(fromUserName).append("\n")
          .append("type:").append(type).append("\n")
          .append("message:").append(message).append("\n")
          .append("time:").append(time).append("\n");
        return sb.toString();
    }
}
