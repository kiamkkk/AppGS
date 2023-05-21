package com.gseek.gs.exce.business.trade;

import com.gseek.gs.common.BillState;
import com.gseek.gs.exce.business.BusinessException;

/**
 * @author Phak
 * @since 2023/5/21-15:20
 */
public class BillStateNotAllowException extends BusinessException {

    /**
     *
     *
     * */
    public BillStateNotAllowException(BillState state) {
        super();
        code=400;
        msg="交易状态为: "+state.getState()+" ,不可进行该操作";
    }

}
