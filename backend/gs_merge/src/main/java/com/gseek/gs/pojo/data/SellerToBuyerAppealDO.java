package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-13 12:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerToBuyerAppealDO {
    private int appealId;
    private String appealReason;
    private String picBefore;
    private String picAfter;
    private boolean accept;
    private int billId;
    private boolean checked;
    private int adminId;
    private String appealResult;
    private int myId;
    private int damageDegree;
}
