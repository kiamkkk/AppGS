package com.gseek.gs.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态.
 *
 * @author Phak
 * @since 2023/5/13-0:07
 */

public enum BillState {

    /**
     * 买家创建订单时默认为待支付
     * */
    PENDING_PAY("待支付"),
    /**
     * 买家支付后
     * */
    PENDING_DELIVER("已支付,待交货"),
    /**
     * 卖家决定交货后
     * */
    PENDING_INSPECT("已交货,待验货"),
    /**
     * 买家验货且无异议
     * */
    TRADE_END("交易结束"),
    /**
     * 买家否定验货后
     * */
    WRONG_INSPECT("买家验货不通过"),
    /**
     * 交易取消
     * */
    TRADE_CANCEL("交易取消"),
    /**
     * 支付超时
     * */
    PAYt_TIMEOUT("支付超时"),
    /**
     * 交易异常
     * */
    TRADE_ANOMALY("交易异常");

    /**
     * 订单情况
     * */
    private final String state;
    /**
     * 枚举与状态值
     * */
    private static final Map<String,BillState> BILL_STATE_MAP =new HashMap<>(16);

    BillState(String state){
        this.state=state;
    }
    public String getState() {
        return state;
    }

    /**
     * 根据状态名获取相应的枚举.
     *
     * @param state 状态名
     * */
    public static BillState gainEnumByState(String state){
        return BILL_STATE_MAP.get(state);
    }

    /**
     * 把所有枚举加载到billStateMap内方便取用
     * */
    static {
        for (BillState value :  BillState.values()) {
            BILL_STATE_MAP.put(value.getState(),value);
        }
    }

}
