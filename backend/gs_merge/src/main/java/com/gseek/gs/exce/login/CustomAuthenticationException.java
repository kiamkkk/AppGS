package com.gseek.gs.exce.login;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Phak
 * @since 2023/5/22-19:47
 */
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
