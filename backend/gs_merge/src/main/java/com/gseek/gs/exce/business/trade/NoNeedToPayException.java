package com.gseek.gs.exce.business.trade;

import com.gseek.gs.exce.business.BusinessException;

/**
 * 订单不需要支付异常.
 * 在TimeoutUtil的billPayState里没有找到订单时抛出.
 *
 * @author Phak
 * @since 2023/5/21-15:26
 */
public class NoNeedToPayException extends BusinessException {

    public NoNeedToPayException() {
        super("该订单不需要支付");
        code=400;
        msg="该订单不需要支付";
    }
}
