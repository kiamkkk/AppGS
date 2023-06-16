package com.gseek.gs.exce.login;

/**
 * 表示用户token不可用.
 *
 * @author Phak
 * @since 2023/5/4-23:50
 */
public class TokenInvalidException extends CustomAuthenticationException {
    public TokenInvalidException() {
        super("TokenInvalidException");
        msg="TokenInvalidException";
    }
}
