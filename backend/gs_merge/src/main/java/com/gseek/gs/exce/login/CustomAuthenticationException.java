package com.gseek.gs.exce.login;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * springSecurity过滤链中的自定义异常基类.
 * 过滤链中的异常只能被过滤链中的组件处理,且只处理AuthenticationException和AccessDeniedException.
 *
 * @author Phak
 * @since 2023/5/22-19:47
 */
@Getter
public class CustomAuthenticationException extends AuthenticationException {
    protected int code;
    protected String msg;

    public CustomAuthenticationException(String message) {
        super(message);
        code=403;
    }
}
