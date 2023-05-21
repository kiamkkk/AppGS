package com.gseek.gs.exce.business.money;

import com.gseek.gs.exce.business.ForbiddenException;

/**
 * 账户余额不足异常.
 *
 * @author Phak
 * @since 2023/5/21-15:03
 */
public class RemainNotEnoughException extends ForbiddenException {

    public RemainNotEnoughException() {
        super();
        msg= "账户余额不足";
    }
}
