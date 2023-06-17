package com.gseek.gs.exce.business.trade;

import com.gseek.gs.exce.business.BusinessException;

/**
 * 商品已经被卖出异常.
 *
 * @author Phak
 * @since 2023/5/21-15:10
 */
public class GoodSoldException extends BusinessException{

    public GoodSoldException() {
        super("商品已经被卖出");
        code=400;
        msg="GoodSold";
    }

}
