package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 包含解析UserController#postRealNameInformation的数据.
 *
 * @author Phak
 * @since 2023/5/6-0:37
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRealNameInformationDTO implements DTOPerService{

    private String userName;
    private String idNumber;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if (userName == null ||userName.isBlank()){
            bean.addParameters("userName",userName);
        }
        if (idNumber == null ||idNumber.isBlank()){
            bean.addParameters("idNumber", idNumber);
        }
        if (time == null || time <= 0){
            bean.addParameters("time", time+"");
        }

        if (! bean.getWrongParameters().isEmpty() ){
            throw new ParameterWrongException(bean);
        }

    }

    @Override
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        idNumber=PasswordUtil.decrypt(idNumber);
    }
}
