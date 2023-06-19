package com.gseek.gs.pojo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int claimerId;
    private int respondentId;
    private String appealReason;
    private boolean checked;
    private String provePic;
    private int blackId;
    public String toMessage( ObjectMapper objectMapper){
        ObjectNode objectNode =objectMapper.createObjectNode();
        objectNode.put("respondentId", respondentId);
        objectNode.put("appealReason", appealReason);
        objectNode.put("blackId",blackId);
        return objectNode.toPrettyString();
    }
}
