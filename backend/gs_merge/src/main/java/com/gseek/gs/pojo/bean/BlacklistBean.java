package com.gseek.gs.pojo.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 16:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistBean {
    private int claimer_id;
    private int respondent_id;
    private String appeal_reason;
    private boolean checked;
    private String provePic;
    private int black_id;
    public String toMessage( ObjectMapper objectMapper){
        ObjectNode objectNode =objectMapper.createObjectNode();
        objectNode.put("respondentId", respondent_id);
        objectNode.put("appealReason", appeal_reason);
        objectNode.put("blackId",black_id);
        return objectNode.toPrettyString();
    }
}
