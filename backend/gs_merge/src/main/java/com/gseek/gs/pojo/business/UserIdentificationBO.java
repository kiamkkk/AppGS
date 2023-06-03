package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.util.PasswordUtil;
import com.gseek.gs.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 封装实名认证信息
 *
 * @author Phak
 * @since 2023/5/5-23:45
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentificationBO implements BOPostService{

    @JsonIgnore
    private Integer userId;
    private String idNumber;

    @Override
    public void autoEncrypt() throws ServerException {
        try {
            idNumber= StrUtil.desensitizeIdNumber(idNumber);
            idNumber=PasswordUtil.encrypt(idNumber);
        }catch (Exception e){
            throw new ServerException("服务器异常", e);
        }

    }
}
