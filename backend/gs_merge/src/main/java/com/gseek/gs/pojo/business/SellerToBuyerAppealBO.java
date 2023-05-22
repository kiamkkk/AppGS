package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-13 12:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerToBuyerAppealBO {
    private int appeal_id;
    private String appeal_reason;
    private String pic_before;
    private String pic_after;
    private boolean accept;
    private int bill_id;
    private boolean checked;
    @JsonIgnore
    private int myId;
}
