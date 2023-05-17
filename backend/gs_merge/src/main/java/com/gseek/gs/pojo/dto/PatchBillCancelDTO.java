package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/13-19:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchBillCancelDTO implements DTOPerService{

    private String billId;
    private Integer sellerId;
    private Boolean cancel;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if (billId ==null || billId.isEmpty()){
            bean.addParameters("billId",billId);
        }
        if (sellerId==null || sellerId==0){
            bean.addParameters("sellerId", sellerId+"");
        }
        if (time == null || time <= 0){
            bean.addParameters("time", String.valueOf(time));
        }
        if (cancel==null){
            bean.addParameters("cancel", null);
        }

        if (! bean.getWrongParameters().isEmpty() ){
              throw new ParameterWrongException(bean);
        }

    }

    @Override
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException {
        billId= PasswordUtil.decrypt(billId);
    }
}
