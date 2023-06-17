package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int appealId;
    private String appealReason;
    private boolean checked;
    private String provePic;
    private int billId;
    @JsonIgnore
    private int myId;
}
