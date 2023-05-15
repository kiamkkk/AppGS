package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/12-23:10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayBillDTO implements DTOPerService{
    private String billId;
    private Long time;
    private Integer buyerId;


    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean=new ParameterWrongBean();
        if (billId==null || billId.isEmpty() ){
            bean.addParameters("订单id", billId);
        }
        if (time==null || time <= 0){
            bean.addParameters("订单支付时间", time+"");
        }
        if (buyerId==null || buyerId <= 0){
            bean.addParameters("支付人id", buyerId+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }

    }

    @Override
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException {
        billId= PasswordUtil.decrypt(billId);
    }
}
