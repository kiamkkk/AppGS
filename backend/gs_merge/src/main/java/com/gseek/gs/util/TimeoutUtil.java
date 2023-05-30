package com.gseek.gs.util;

import com.gseek.gs.config.TimeoutConfig;
import com.gseek.gs.exce.business.trade.NoNeedToPayException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 超时自动操作
 *
 * @author Phak
 * @since 2023/5/13-16:31
 */
@Component("timeoutUtil")
public class TimeoutUtil {
    //todo 补充注释

    /**
     * 默认待支付时间30分钟,考虑到执行操作延迟，后端预留一分钟
     * */
    public static final long PAY_TIME=31*60*1000;

    @Autowired
    RabbitTemplate template;

    /**
     * 订单支付状态map,记录订单支付状态.
     * */
    private Map<String, Boolean> billPayState=new HashMap<>(16);


    /**
     * 向订单支付计时队列插入一个订单，并向订单支付状态map添加订单.
     * */
    public void offerBill(String billId){
        billPayState.put(billId, false);
        template.convertAndSend(
                TimeoutConfig.EXCHANGE_INNER_PAY,
                TimeoutConfig.KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_EXCHANGE ,
                billId);

    }

    /**
     * 订单支付后修改
     * */
    public void popBill(String billId)
        throws NoNeedToPayException{
        if ( billPayState.containsKey(billId) ){
            billPayState.remove(billId);
        }else {
            // 该订单不需要支付
            throw new NoNeedToPayException();
        }
    }


}
