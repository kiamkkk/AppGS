package com.gseek.gs.exce.business.common;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/6/4-21:30
 *
 * @deprecated JWTs原有的ExpiredJwtException就够用了,这个异常没有必要出现.
 */
public class TokenExpiredException extends BusinessException {

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
