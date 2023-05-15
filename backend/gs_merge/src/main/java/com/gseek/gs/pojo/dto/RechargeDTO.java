package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/15-11:35
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RechargeDTO implements DTOPerService{

    private BigDecimal chargeAmount;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean=new ParameterWrongBean();
        //充值金额大于0.01元小于1亿元
        if (chargeAmount == null ||
            chargeAmount.compareTo(new BigDecimal("0.00")) < 1 ||
            chargeAmount.compareTo(new BigDecimal("100000000.00")) > 0){
            bean.addParameters("chargeAmount",chargeAmount.toString());
        }

        if (time==null||time<=0){
            bean.addParameters("time",time.toString());
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
