package com.gseek.gs.config;

import com.gseek.gs.util.TimeoutUtil;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 交易延迟支付.
 *
 * @author Phak
 * @since 2023/5/15-20:20
 */
@Configuration
public class TimeoutConfig {

    /**
     *
     * */
    public static final String EXCHANGE_INNER_PAY="innerPayExchange";
    public static final String EXCHANGE_INNER_PAY_TIMEOUT="innerPayTimeoutExchange";

    public static final String QUEUE_PENDING_PAY="pendingPayQueue";
    public static final String QUEUE_TIMEOUT="payTimeoutQueue";
    //todo 给路由键取个像样点的名字吧。。。
    /**
     * 支付队列绑定死信队列
     * */
    public static final String KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_TIMEOUT_EXCHANGE ="PPQ-IPTE";
    public static final String KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_EXCHANGE ="PPQ-IPE";

    @Bean("innerPayExchange")
    public DirectExchange innerPayExchange(){
        return new DirectExchange(EXCHANGE_INNER_PAY);
    }
    @Bean("innerPayTimeoutExchange")
    public DirectExchange innerPayTimeoutExchange(){
        return new DirectExchange(EXCHANGE_INNER_PAY_TIMEOUT);
    }
    /**
     * 声明pendingPayQueue ttl 为 10s 并绑定到对应的死信交换机innerPayTimeoutExchange
     * */
    @Bean("pendingPayQueue")
    public Queue pendingPay(){
        Map<String, Object> args = new HashMap<>(16);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", EXCHANGE_INNER_PAY_TIMEOUT);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_TIMEOUT_EXCHANGE);
        //声明队列的 TTL
        args.put("x-message-ttl", TimeoutUtil.PAY_TIME);
        return QueueBuilder.durable(QUEUE_PENDING_PAY).withArguments(args).build();
    }

    /**
     * 声明pendingPayQueue 绑定 innerPayExchange
     * */
    @Bean
    public Binding pendingPayQueueBindingInnerPayExchange(@Qualifier("pendingPayQueue") Queue pendingPayQueue,
                                  @Qualifier("innerPayExchange") DirectExchange innerPayExchange){
        return BindingBuilder.bind(pendingPayQueue).to(innerPayExchange)
                .with(KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_EXCHANGE);
    }
    /**
     * 声明死信队列 payTimeoutQueue
     * */
    @Bean("payTimeoutQueue")
    public Queue payTimeoutQueue(){
        return new Queue(QUEUE_TIMEOUT);
    }
    /**
     * 声明死信队列  payTimeoutQueue 绑定关系
     * */
    @Bean
    public Binding payTimeoutQueueBindingInnerPayTimeoutExchange(@Qualifier("payTimeoutQueue") Queue payTimeoutQueue,
                                        @Qualifier("innerPayTimeoutExchange") DirectExchange innerPayTimeoutExchange){
        return BindingBuilder.bind(payTimeoutQueue).to(innerPayTimeoutExchange)
                .with(KEY_PENDING_PAY_QUEUE_TO_INNER_PAY_TIMEOUT_EXCHANGE);
    }


}
