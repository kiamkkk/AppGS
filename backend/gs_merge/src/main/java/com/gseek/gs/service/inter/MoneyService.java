package com.gseek.gs.service.inter;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.money.RemainNotEnoughException;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/13-17:08
 */
public interface MoneyService {

    /**
     *
     * 1.获取所需金额
     * 2.判断买家余额是否足够，不够会抛出异常
     * 3.修改余额
     *
     * */
    void payBill(int buyerId, int billId);

    /**
     *  根据订单对应的商品价格还给买家钱
     *
     */
    void returnMoney(int billId, int sellerId) throws RemainNotEnoughException;
    /**
     *
     * */
    void payToSeller(int billId, int buyerId);

    /**
     * 充值
     * */
    String recharge(int userId,RechargeDTO rechargeDTO) throws JsonProcessingException, AlipayApiException;

    /**
     * 提现
     * */
    String withdrawal(int userId, WithdrawalDTO withdrawalDTO)
            throws JsonProcessingException, AlipayApiException, FileNotFoundException;

    /**
     * 查看钱包数据
     * */
    String getWalletInformation(int userId) throws JsonProcessingException;
    int frozenUser(int userId);
    int unfrozenUser(int userId);
}
