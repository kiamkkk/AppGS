package com.gseek.gs.exce.business.login;

/**
 * @author Phak
 * @since 2023/5/3-14:13
 */
public class RepeatLoginException extends CustomAuthenticationException {

    public RepeatLoginException() {
        super("RepeatLogin");
    }
}
