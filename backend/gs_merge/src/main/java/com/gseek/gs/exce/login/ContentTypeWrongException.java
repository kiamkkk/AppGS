package com.gseek.gs.exce.login;

/**
 * @author Phak
 * @since 2023/5/3-14:38
 */
public class ContentTypeWrongException extends CustomAuthenticationException {

    public ContentTypeWrongException() {
        super("ContentTypeWrong");
    }
}
