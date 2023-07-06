package com.gseek.gs.exce.business.common;

import com.gseek.gs.exce.business.BusinessException;

/**
 * 表示用户没有权限进行操作.
 *
 * @author Phak
 * @since 2023/5/5-22:30
 */
public class ForbiddenException extends BusinessException {

    /**
     * @deprecated 见父类
     * */
    public ForbiddenException() {
        super("Forbidden");
        code=403;
    }

    public ForbiddenException(String message) {
        super(message);
        code=403;
    }

    /**
     * 用户访问非自己名下的数据时使用.
     * */
    public static ForbiddenException gainNotAccess(){
       return new ForbiddenException("Forbidden");
    }
}
