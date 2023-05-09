package com.gseek.gs.pojo.data;

import lombok.*;

import java.util.Date;
/**
 * good实体类
 *
 * @author Isabella
 * @since 2023/5/2-19:20
 */
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
