package com.gseek.gs.service.impl;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.dao.RechargeWithdrawMapper;
import com.gseek.gs.exce.ToBeConstructed;
import com.gseek.gs.pojo.business.MoneyBO;
import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.util.AlipayUtil;
import com.gseek.gs.util.StrUtil;
import com.gseek.gs.util.TimeoutUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/13-17:08
 */
@Service("moneyServiceImpl")
public class MoneyServiceImpl implements MoneyService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AlipayUtil alipayUtil;
    @Autowired
    Result result;
    @Autowired
    TimeoutUtil timeoutUtil;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    BillMapper billMapper;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    RechargeWithdrawMapper rwMapper;

    @Override
    public void payBill(int buyerId, int billId) {
        BigDecimal remain=moneyMapper.selectRemainByUserId(buyerId);
        BigDecimal price=goodMapper.selectPriceByBillId(billId);
        if ( remain.compareTo(price) <= 0 ){
            //todo 余额不足，新建异常
            throw new ToBeConstructed();
        }
        moneyMapper.minusRemainByUserId(buyerId,price);
    }

    @Override
    public void returnMoney(int billId) {
        // todo 判断余额是否充足
        moneyMapper.returnMoney(billId);
    }

    @Override
    public void payToSeller(int billId) {
        // todo 判断余额是否充足
        moneyMapper.payToSeller(billId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String recharge(int userId, RechargeDTO dto)
            throws JsonProcessingException, AlipayApiException {
        // todo 鉴权！！！

        String rwId= StrUtil.alipayOutTradeNoWarp(userId);
        RechargeWithdrawDO rwdo=new RechargeWithdrawDO(rwId,userId,dto);
        AlipayUtil.payMap.put(rwId,rwdo);

        // 用户在支付宝里充值
        alipayUtil.alipayTradePagePay(rwId,dto.getChargeAmount());

        return result.gainPostSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String withdrawal(int userId, WithdrawalDTO dto)
            throws JsonProcessingException, AlipayApiException, FileNotFoundException {
        // todo 鉴权！！！

        BigDecimal accountRemain = moneyMapper.selectRemainByUserId(userId);
        if (accountRemain.compareTo(dto.getWithdrawalAmount()) >= 1){
            //todo 账户余额不足
            throw new ToBeConstructed();
        }

        String rwId= StrUtil.alipayOutTradeNoWarp(userId);
        RechargeWithdrawDO rwdo=new RechargeWithdrawDO(rwId,userId,dto);
        // 支付宝转账
        alipayUtil.alipayFundTransUniTransfer(rwId, dto.getWithdrawalAmount(), dto.getAlipayAccount());
        // 修改本地余额
        moneyMapper.minusRemainByUserId(userId,dto.getWithdrawalAmount());
        rwMapper.insertRecode(rwdo);
        return result.gainPatchSuccess();
    }

    @Override
    public String getWalletInformation(int userId)
            throws JsonProcessingException {
        MoneyBO bo=moneyMapper.selectMoneyBOByUserId(userId);
        return objectMapper.writeValueAsString(bo);
    }
}
