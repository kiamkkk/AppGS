package com.gseek.gs.websocket.message;

/**
 * 封装交货信息。
 *
 * @author Phak
 * @since 2023/5/8-22:30
 */
public class DeliveryMessage {

    private int goodId;
    private String goodName;
    private String accountName;
    private String accountPassword;

    public DeliveryMessage(int goodId, String goodName, String accountName, String accountPassword) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.accountName = accountName;
        this.accountPassword = accountPassword;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
}
