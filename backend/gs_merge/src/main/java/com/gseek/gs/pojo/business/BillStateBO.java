package com.gseek.gs.pojo.business;

import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/13-18:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillStateBO implements BOPostService{

    private String billId;
    private Integer goodId;
    private Long createTime;
    private BigDecimal price;
    private String sellerName;
    private String buyerName;
    private String state;
    private Long stateModifiedTime;

    @Override
    public void autoEncrypt() throws IllegalBlockSizeException, BadPaddingException {
        billId= PasswordUtil.encrypt(billId);
    }
}
