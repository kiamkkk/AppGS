package com.gseek.gs.exce.business.common;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/5/5-22:30
 */
public class ForbiddenException extends BusinessException {

    /**
     * @deprecated 见父类
     * */
    public ForbiddenException() {
        super();
        code=403;
    }

    public ForbiddenException(String message) {
        super(message);
        code=403;
    }
}
