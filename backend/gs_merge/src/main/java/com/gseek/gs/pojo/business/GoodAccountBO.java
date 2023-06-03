package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/13-18:10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodAccountBO implements BOPostService{
    @JsonIgnore
    private Integer buyerId;
    private Integer goodId;
    private String goodName;
    private String account;
    private String accountPassword;

    @Override
    public void autoEncrypt() throws ServerException {
        account= PasswordUtil.decrypt(account);
        accountPassword=PasswordUtil.decrypt(accountPassword);
    }

    public String toMessage( ObjectMapper objectMapper){
        ObjectNode objectNode =objectMapper.createObjectNode();
        objectNode.put("goodId", goodId);
        objectNode.put("goodName", goodName);
        objectNode.put("account",account);
        objectNode.put("accountPassword", accountPassword);
        return objectNode.toPrettyString();
    }
}
