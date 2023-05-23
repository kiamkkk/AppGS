package com.gseek.gs.exce.business.users;

import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/5/23-18:31
 */
public class RepeatUserNameException extends BusinessException {

    public RepeatUserNameException(Exception e) {
        super(e);
        code=400;
        msg="RepeatUserName";
    }
}
