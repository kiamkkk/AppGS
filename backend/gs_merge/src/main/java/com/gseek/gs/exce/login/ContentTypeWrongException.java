package com.gseek.gs.exce.login;

/**
 * 登录时抛出.
 * 只允许content-type为application-json的请求.
 *
 * @author Phak
 * @since 2023/5/3-14:38
 */
public class ContentTypeWrongException extends CustomAuthenticationException {
    public ContentTypeWrongException() {
        super("ContentTypeWrong");
        msg="ContentTypeWrong";
    }
}
