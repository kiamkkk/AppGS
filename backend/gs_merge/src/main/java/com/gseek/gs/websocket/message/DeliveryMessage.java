package com.gseek.gs.websocket.message;

import com.gseek.gs.pojo.business.GoodAccountBO;
import lombok.Getter;
import lombok.Setter;

/**
 * 封装交货信息。
 *
 * @author Phak
 * @since 2023/5/8-22:30
 */
@Setter
@Getter
public class DeliveryMessage {

    private int goodId;
    private String goodName;
    private String accountName;
    private String accountPassword;

    public DeliveryMessage(GoodAccountBO bo) {
        this.goodId= bo.getGoodId();
        this.goodName=bo.getGoodName();
        this.accountName= bo.getAccount();
        this.accountPassword= bo.getAccountPassword();
    }
}
