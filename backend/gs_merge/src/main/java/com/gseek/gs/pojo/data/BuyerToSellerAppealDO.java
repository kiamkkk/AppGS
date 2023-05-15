package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerToSellerAppealDO {
    private int appealId;
    private String appealReason;
    private boolean appealResult;
    private boolean checked;
    private int adminId;
    private String provePic;
    private int billId;
    private int myId;
}
