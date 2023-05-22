package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-19 21:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodCheckedDO {
    private int goodId;
    private int adminId;
    private boolean checked;
    private Long checkTime;
    private boolean result;
    private String disapproveReason;
}
