package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gseek.gs.util.PasswordUtil;
import com.gseek.gs.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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
@Component
public class UserIdentificationBO implements BOPostService{

    @Autowired
    @JsonIgnore
    PasswordUtil passwordUtil;

    @JsonIgnore
    private Integer userId;
    private String idNumber;

    @Override
    public void autoEncrypt() throws IllegalBlockSizeException, BadPaddingException {
        idNumber= StrUtil.desensitizeIdNumber(idNumber);
        idNumber=PasswordUtil.encrypt(idNumber);
    }
}
