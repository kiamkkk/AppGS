package com.gseek.gs.pojo.business;

import com.gseek.gs.exce.ServerException;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public void autoEncrypt() throws ServerException {
        try {
            billId= PasswordUtil.encrypt(billId);
        }catch (Exception e){
            throw new ServerException("服务器异常", e);
        }
    }
}
