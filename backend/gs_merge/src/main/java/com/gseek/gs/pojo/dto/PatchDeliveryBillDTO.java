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
 * @since 2023/5/13-17:34
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchDeliveryBillDTO implements DTOPerService{

    private String billId;
    private Boolean delivered;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if (billId ==null || billId.isEmpty()){
            bean.addParameters("billId", billId);
        }
        if (delivered == null){
            bean.addParameters("delivered", String.valueOf(delivered));
        }
        if (time == null || time <= 0){
            bean.addParameters("time", String.valueOf(time));
        }
        if (! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }

    @Override
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException {
        billId= PasswordUtil.decrypt(billId);
    }
}
