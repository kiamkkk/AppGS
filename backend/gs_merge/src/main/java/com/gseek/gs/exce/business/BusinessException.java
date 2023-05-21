package com.gseek.gs.exce.business;

import com.gseek.gs.exce.BaseException;

/**
 * 自定义业务异常.
 * 所有自定义业务异常都要直接或间接地继承自该类,并在ExceptionController中统一处理
 *
 * @author Phak
 * @since 2023/5/2-20:55
 */
public class BusinessException extends BaseException {

    public BusinessException() {
        super();
    }

    public BusinessException(String... info) {
        super(info);
    }

    public BusinessException(Throwable cause, String... info) {
        super(cause, info);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String... info) {
        super(message, cause, enableSuppression, writableStackTrace, info);
    }
}
