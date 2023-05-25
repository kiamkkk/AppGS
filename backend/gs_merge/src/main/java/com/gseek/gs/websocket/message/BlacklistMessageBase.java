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
public class BlacklistMessageBase {
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
}
