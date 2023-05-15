package com.gseek.gs.pojo.business;

import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/13-18:10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodAccountBO implements BOPostService{

    private Integer goodId;
    private String goodName;
    private String account;
    private String accountPassword;

    @Override
    public void autoEncrypt() throws IllegalBlockSizeException, BadPaddingException {
        account= PasswordUtil.decrypt(account);
        accountPassword=PasswordUtil.decrypt(accountPassword);
    }
}
