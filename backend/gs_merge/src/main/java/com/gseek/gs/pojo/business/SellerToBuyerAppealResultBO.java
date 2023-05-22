package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private boolean appeal_result;
    private int damage_degree;
    @JsonIgnore
    private boolean accept;
    @JsonIgnore
    private int appealId;
}
