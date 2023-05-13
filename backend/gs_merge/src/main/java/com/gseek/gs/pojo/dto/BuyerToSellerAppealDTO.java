package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 15:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerToSellerAppealDTO {

    private String appealReason;
    private String provePic;
    private int billId;

    private int myId;


}
