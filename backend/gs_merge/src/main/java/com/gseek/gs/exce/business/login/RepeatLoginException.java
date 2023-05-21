package com.gseek.gs.exce.business.login;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/5/3-14:13
 */
public class RepeatLoginException extends BusinessException {

    public RepeatLoginException(String... info) {
        super();
        code=400;
        msg="RepeatLogin:PleaseLogout";
    }
}
