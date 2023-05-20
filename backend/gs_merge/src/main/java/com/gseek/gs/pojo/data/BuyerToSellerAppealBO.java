package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 16:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerToSellerAppealBO {
    private int appeal_id;
    private String appeal_reason;
    private boolean checked;
    private String provePic;
    private int bill_id;
}
