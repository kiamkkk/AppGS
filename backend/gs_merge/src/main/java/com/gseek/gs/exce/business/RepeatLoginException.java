package com.gseek.gs.exce.business;

/**
 * @author Phak
 * @since 2023/5/3-14:13
 */
public class RepeatLoginException extends BusinessException{

    public RepeatLoginException(String... info) {
        super(info);
        code=400;
        msg="RepeatLogin:PleaseLogout";
    }
}
