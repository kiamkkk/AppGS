package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author Phak
 * @since 2023/5/9-11:40
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PatchUserInformationDTO implements DTOPerService{
    // todo 要支持修改密码！！！
    private String email;
    private String username;
    private MultipartFile picture;

    public PatchUserInformationDTO(HttpServletRequest request) {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        this.picture=params.getFile("picture");
        if (picture == null || picture.getOriginalFilename().isBlank() ){
            picture = null;
        }
        this.email=params.getParameter("email");
        this.username=params.getParameter("username");
    }

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if ( email!=null && ! email.isBlank() && ! StrUtil.checkEmail(email)){
            bean.addParameters("email",email);
        }
        if (username!=null && ! username.isBlank() && ! StrUtil.checkUserName(username)){
            bean.addParameters("username", username);
        }

        if (! bean.getWrongParameters().isEmpty() ){
            throw new ParameterWrongException(bean);
        }

    }
}
