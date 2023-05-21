package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/15-16:25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDTO implements DTOPerService{

    private Long time;
    private BigDecimal withdrawalAmount;
    private String alipayAccount;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();
        //充值金额大于0.01元小于1亿元
        if (withdrawalAmount == null ||
                withdrawalAmount.compareTo(new BigDecimal("0.00")) < 1 ||
                withdrawalAmount.compareTo(new BigDecimal("100000000.00")) > 0){
            bean.addParameters("chargeAmount",withdrawalAmount.toString());
        }

        if (time==null||time<=0){
            bean.addParameters("time",time.toString());
        }

        if (alipayAccount==null ||alipayAccount.isEmpty()){
            bean.addParameters("alipayAccount",alipayAccount);
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
