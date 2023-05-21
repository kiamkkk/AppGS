package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import com.gseek.gs.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Phak
 * @since 2023/5/9-11:40
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserInformationDTO implements DTOPerService{
    private String email;
    private String username;
    private MultipartFile picture;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if ( email!=null && StrUtil.checkEmail(email)){
            bean.addParameters("email",email);
        }
        if (username!=null && StrUtil.checkUserName(username)){
            bean.addParameters("username", username);
        }

        if (! bean.getWrongParameters().isEmpty() ){
            throw new ParameterWrongException(bean);
        }

    }
}
