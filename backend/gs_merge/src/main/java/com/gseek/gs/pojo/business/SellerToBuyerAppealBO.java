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
    private int appealId;
    private String appealReason;
    private String picBefore;
    private String picAfter;
    private boolean accept;
    private int billId;
    private boolean checked;
    @JsonIgnore
    private int myId;
}
