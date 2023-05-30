package com.gseek.gs.exce.business.login;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Phak
 * @since 2023/5/4-23:50
 */
@Slf4j
public class TokenInvalidException extends CustomAuthenticationException {

    public TokenInvalidException() {
        super("TokenInvalidException");
        log.error("TokenInvalidException");
    }
}
