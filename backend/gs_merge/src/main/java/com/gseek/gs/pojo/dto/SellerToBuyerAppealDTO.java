package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-13 12:34
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerToBuyerAppealDTO {
    private String appealReason;
    private String picBefore;
    private String picAfter;
    private boolean accept;
    private int billId;
    private int myId;


}
