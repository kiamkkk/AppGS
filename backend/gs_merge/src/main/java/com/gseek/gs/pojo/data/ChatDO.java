package com.gseek.gs.pojo.data;

import com.gseek.gs.websocket.message.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Phak
 * @since 2023/5/17-9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDO {

    private Integer toUserId;
    private Integer fromUserId;
    private Integer goodId;
    private String text;
    private Long sendTime;

    public ChatDO(BaseMessage message) {
        this.fromUserId=message.getFromUserId();
        this.toUserId=message.getToUserId();
        this.sendTime=message.getTime();
        this.text=message.getMessage();
        this.goodId=message.getGoodId();
    }
}
