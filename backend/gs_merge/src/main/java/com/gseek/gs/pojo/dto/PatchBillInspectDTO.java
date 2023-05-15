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
 * @since 2023/5/13-20:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchBillInspectDTO implements DTOPerService{

    private String billId;
    private Integer buyerId;
    private Boolean inspected;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if (billId ==null || billId.isEmpty()){
            bean.addParameters("billId",billId);
        }
        if (buyerId==null || buyerId==0){
            bean.addParameters("sellerId", buyerId+"");
        }
        if (time == null || time <= 0){
            bean.addParameters("time", String.valueOf(time));
        }
        if (inspected==null){
            bean.addParameters("inspected", null);
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
