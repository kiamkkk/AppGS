package com.gseek.gs.exce.business.money;

import com.gseek.gs.exce.business.ForbiddenException;

/**
 * 钱包被冻结异常
 *
 * @author Phak
 * @since 2023/5/21-15:08
 */
public class WalletFrozenException extends ForbiddenException {

    public WalletFrozenException() {
        super();
        msg="钱包已被冻结";
    }
}