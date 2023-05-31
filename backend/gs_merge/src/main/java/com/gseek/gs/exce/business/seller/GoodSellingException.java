package com.gseek.gs.exce.business.seller;

import com.gseek.gs.exce.business.BusinessException;

/**
 * 表示商品正在交易,禁止更改.
 * 应该在卖家尝试修改、删除商品时检查bill表中有无对应的goodId,有则抛出这个异常.
 *
 * @author Phak
 * @since 2023/5/31-23:56
 */
public class GoodSellingException extends BusinessException {

    public GoodSellingException() {
        code=400;
        msg="Good is Selling";
    }

}
