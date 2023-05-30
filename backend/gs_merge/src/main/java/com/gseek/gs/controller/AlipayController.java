package com.gseek.gs.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.gseek.gs.config.AlipayConfig;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.dao.RechargeWithdrawMapper;
import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import com.gseek.gs.util.AlipayUtil;
import com.gseek.gs.util.TimeoutUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付宝应用网关.
 *
 * @author Phak
 * @since 2023/5/14-15:13
 */
@RestController
@Slf4j
@RequestMapping("/alipay")
public class AlipayController {

    @Autowired
    TimeoutUtil timeoutUtil;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    RechargeWithdrawMapper rwMapper;

    /**
     * 异步通知,充值成功后被调用.
     */
    @RequestMapping("/notify")
    public String notify(Map<String, String> paramsMap) {
        try {
            boolean signVerified = AlipaySignature
                    .rsaCheckV1(paramsMap, AlipayConfig.appPublicKey,
                            AlipayConfig.charset, AlipayConfig.signType);
            if (signVerified) {
                log.debug("支付宝回调签名认证成功");
                String tradeStatus= paramsMap.get("trade_status");
                log.debug("trade_status:{}" +tradeStatus);
                // 支付成功
                if (tradeStatus.equals("TRADE_SUCCESS")) {
                    // 处理支付成功逻辑
                    try {
                        RechargeWithdrawDO rwdo= AlipayUtil.payMap.get(paramsMap.get("out_trade_no"));
                        // 修改本地余额
                        moneyMapper.addRemainByUserId(rwdo.getUserId(),rwdo.getAmount());
                        rwMapper.insertRecode(rwdo);
                        AlipayUtil.payMap.remove(rwdo.getRwId());
                    } catch (Exception e) {
                        log.error("支付宝回调业务处理报错:{}",e.getMessage());
                    }
                } else {
                    log.warn("没有处理支付宝回调业务，支付宝交易状态：{}", tradeStatus);
                }
                return "success";
            } else {
                // 验签失败
                log.warn("支付宝回调签名认证失败");
                return "failure";
            }
        }catch (AlipayApiException e){
            log.warn("支付宝回调签名认证失败,errorMsg:{}", e.getMessage());
            return "failure";
        }
    }

    /**
     * 充值成功页面
     */
    @RequestMapping("/pay_success")
    public String paySuccess(Map<String, String> paramsMap){
        //todo 官网说：于同步返回的不可靠性，支付结果必须以异步通知或查询接口返回为准，不能依赖同步跳转。
        //     所以这要怎么处理 ??????
        return "success";
    }

}
