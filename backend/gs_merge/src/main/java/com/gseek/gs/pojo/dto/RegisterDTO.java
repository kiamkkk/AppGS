package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/20-16:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO implements DTOPerService{

    private String userName;
    private String password;
    private String email;
    private Long time;


    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();

        if (userName.isBlank() || StrUtil.checkUserName(userName)){
            bean.addParameters("userName", userName);
        }
        if (password.isBlank()){
            bean.addParameters("password", password);
        }
        if (email.isBlank()){
            bean.addParameters("email", email);
        }
        if (time==null || time==0){
            bean.addParameters("time", time+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }

}
