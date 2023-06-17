package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 20:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerToSellerAppealResultBO {
    private boolean appealResult;
    private boolean checked;
    private int appealId;
}
