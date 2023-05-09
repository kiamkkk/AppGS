package com.gseek.gs.pojo.dto;

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
public class PostRealNameInformationDTO {
    //todo 修改dto，把userId放进去
    //todo 在这把身份证号解密

    private String userName;
    private String rawIdNumber;
    private long time;

    public void setRawIdNumber(String rawIdNumber) throws IllegalBlockSizeException, BadPaddingException {
        this.rawIdNumber = PasswordUtil.decrypt(rawIdNumber);
    }


}
