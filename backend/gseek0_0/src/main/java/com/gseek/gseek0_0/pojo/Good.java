package com.gseek.gseek0_0.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good {
    private int goodId;
    private String goodName;
    private String account;
    private String accountPassword;
    private String goodText;
    private int ownUserId;
    private double goodPrice;
    private long createTime;
    private long modifiedTime;
    private boolean sold;
}
