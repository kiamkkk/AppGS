package com.gseek.gseek0_0.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class bill {
    private int billId;
    private int goodId;
    private long createTime;
    private boolean completed;
    private int sellerId;
    private int buyerId;
    private boolean delivered;
    private long deliveredTime;
    private boolean inspected;
    private long inspectedTime;
}
