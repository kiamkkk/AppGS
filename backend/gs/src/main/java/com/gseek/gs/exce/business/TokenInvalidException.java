package com.gseek.gs.exce.business;

/**
 * @author Phak
 * @since 2023/5/4-23:50
 */
public class TokenInvalidException extends BusinessException {

    public TokenInvalidException(String... info) {
        super(info);
        code=403;
        msg="TokenInvalid";
    }
}
