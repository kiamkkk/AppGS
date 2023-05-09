package com.gseek.gs.exce.business;

/**
 * @author Phak
 * @since 2023/5/5-22:30
 */
public class ForbiddenException extends BusinessException{

    public ForbiddenException(String... info) {
        super(info);
        code=403;
        msg="Forbidden";
    }
}