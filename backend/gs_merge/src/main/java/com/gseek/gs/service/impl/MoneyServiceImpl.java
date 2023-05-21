package com.gseek.gs.service.impl;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.dao.RechargeWithdrawMapper;
import com.gseek.gs.exce.business.money.RemainNotEnoughException;
import com.gseek.gs.exce.business.money.WalletFrozenException;
import com.gseek.gs.pojo.business.MoneyBO;
import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.util.AlipayUtil;
import com.gseek.gs.util.StrUtil;
import com.gseek.gs.util.TimeoutUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/13-17:08
 */
@Service("moneyServiceImpl")
@Slf4j
public class MoneyServiceImpl implements MoneyService {
    /**
     * 平台抽成后卖家获得多少钱
     * */
    @Value("${custom.agreementFee}")
    public BigDecimal agreementFee;
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
    public void payBill(int buyerId, int billId)
            throws RemainNotEnoughException {
        BigDecimal remain=moneyMapper.selectRemainByUserId(buyerId);
        BigDecimal price=remainAvailable(buyerId);
        if ( remain.compareTo(price) <= 0 ){
            throw new RemainNotEnoughException();
        }
        moneyMapper.minusRemainByUserId(buyerId,price);
    }

    @Override
    public void returnMoney(int billId, int sellerId)
            throws RemainNotEnoughException {
        // 判断卖家余额是否充足
        BigDecimal goodPrice=goodMapper.selectPriceByBillId(billId);
        BigDecimal sellerIdRemain=moneyMapper.selectRemainByUserId(sellerId);
        if (goodPrice.compareTo(sellerIdRemain.multiply(agreementFee)) < 0){
            throw new RemainNotEnoughException();
        }
        moneyMapper.returnMoney(billId);
    }

    @Override
    public void payToSeller(int billId, int buyerId)
        throws RemainNotEnoughException{
        // 判断买家余额是否充足
        BigDecimal goodPrice=goodMapper.selectPriceByBillId(billId);
        BigDecimal buyerIdRemain=remainAvailable(buyerId);
        if (goodPrice.compareTo(buyerIdRemain.multiply(agreementFee)) < 0){
            throw new RemainNotEnoughException();
        }
        moneyMapper.payToSeller(billId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String recharge(int userId, RechargeDTO dto)
            throws JsonProcessingException, AlipayApiException {

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
            throws JsonProcessingException, AlipayApiException, FileNotFoundException, RemainNotEnoughException {

        BigDecimal accountRemain = remainAvailable(userId);
        if (accountRemain.compareTo(dto.getWithdrawalAmount()) >= 1){
            throw new RemainNotEnoughException();
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

    /**
     *
     *
     * */
    //todo 补充注释
    private BigDecimal remainAvailable(int userId){
        MoneyBO moneyBO=moneyMapper.selectMoneyBOByUserId(userId);
        if (moneyBO.getFrozen()){
            log.info("用户id{}的钱包已经被冻结",userId);
            throw new WalletFrozenException();
        }
        return  moneyBO.getRemain();
    }
}
