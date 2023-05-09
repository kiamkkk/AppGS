package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserInformationDTO {
    private String email;
    private String username;
    private MultipartFile picture;

    public void setEmail(String email) {
        if (StrUtil.checkEmail(email)){
            //todo 写入错误信息
            throw new ParameterWrongException();
        }
        this.email = email;
    }

    public void setUsername(String username) {
        if (StrUtil.checkUserName(username)){
            //todo 写入错误信息
            throw new ParameterWrongException();
        }
        this.username = username;
    }
}
