package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-13 15:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerToBuyerAppealResultBO {
    private boolean checked;
    private int admin_id;
    private String appeal_result;
    private int damage_degree;
}
