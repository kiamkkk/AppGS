package com.gseek.gs.exce.login;

/**
 * 表示用户重复登录.
 *
 * @author Phak
 * @since 2023/5/3-14:13
 */
public class RepeatLoginException extends CustomAuthenticationException {
    public RepeatLoginException() {
        super("RepeatLogin");
        msg="RepeatLogin";
    }
}
