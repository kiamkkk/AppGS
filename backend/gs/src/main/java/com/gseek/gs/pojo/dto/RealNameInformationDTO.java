package com.gseek.gs.pojo.dto;

import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RealNameInformationDTO {
    //todo 修改dto，把userId放进去
    //todo 在这把身份证号解密

    @Autowired
    PasswordUtil passwordUtil;

    private String userName;
    private String rawIdNumber;
    private long time;

    public void setRawIdNumber(String rawIdNumber) {
        this.rawIdNumber = passwordUtil.decode(rawIdNumber);
    }
}
