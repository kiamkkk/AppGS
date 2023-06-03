package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.util.PasswordUtil;
import com.gseek.gs.util.StrUtil;
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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatchDeliveryBillDTO implements DTOPerService{

    private String billId;
    private Boolean delivered;
    private Long time;

    @JsonIgnore
    private Integer intBillId;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
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
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        billId= PasswordUtil.decrypt(billId);

        ParameterWrongBean bean=new ParameterWrongBean();

        if (StrUtil.checkBillId(billId)){
            bean.addParameters("billId错误", "billId错误");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }

        intBillId=Integer.parseInt(billId);
    }
}
