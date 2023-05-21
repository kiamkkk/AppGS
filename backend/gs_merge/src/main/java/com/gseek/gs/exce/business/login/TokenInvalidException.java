package com.gseek.gs.exce.business.login;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/5/4-23:50
 */
public class TokenInvalidException extends BusinessException {

    public TokenInvalidException(String... info) {
        super();
        code=403;
        msg="TokenInvalid";
    }
}
