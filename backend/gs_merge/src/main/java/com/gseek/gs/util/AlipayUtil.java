package com.gseek.gs.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.AlipayConfig;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.ToBeConstructed;
import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//todo 要实现：充值、提现

/**
 * 封装支付宝操作.
 *
 * @author Phak
 * @since 2023/5/15-0:17
 */
@Component
@Slf4j
public class AlipayUtil {
    private static AlipayClient alipayClient;

    /**
     * 充值待支付
     * */
    public static Map<String, RechargeWithdrawDO> payMap=new HashMap(16);
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        alipayClient = new DefaultAlipayClient(
                AlipayConfig.GATEWAY_URL, AlipayConfig.APP_ID, AlipayConfig.MERCHANT_PRIVATE_KEY,
                "json", AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGN_TYPE);
    }

    /**
     * alipay.trade.page.pay: 统一收单下单并支付页面接口（PC场景下单并支付）.
     *
     * @param outTradeId：平台内部充值订单id.
     * @param amount：支付金额.
     */
    public void alipayTradePagePay(String outTradeId, BigDecimal amount)
            throws AlipayApiException, JsonProcessingException {
        // 封装bizmodel信息
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeId);
        model.setTotalAmount(amount.toString());
        model.setSubject("Gseek电脑网站充值");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setTimeoutExpress(AlipayConfig.TIME_OUT);
        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
        alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        alipayRequest.setBizModel(model);
        // 请求
        AlipayTradePagePayResponse response= alipayClient.pageExecute(alipayRequest);

        if (response.isSuccess()){
            log.debug("充值成功");
        }else {
            //todo 处理一大堆支付宝传来的异常状态码
            String subCode=response.getSubCode();
            log.warn("充值失败，电脑网站支付状态码为:\n{}\n状态信息为:\n{}",subCode,response.getSubMsg());

            //todo 充值失败异常
            throw new ToBeConstructed();
        }
    }

    /**
     * alipay.fund.trans.uni.transfer: 支付宝提现.
     *
     * @param outBizNo     平台内部提现订单id.
     * @param amount       提现金额.
     * @param identity     用户支付宝登录号.
     * @throws
     */
    public void alipayFundTransUniTransfer(String outBizNo, BigDecimal amount, String identity)
            throws ServerException, FileNotFoundException, AlipayApiException, JsonProcessingException {
        //todo 见https://blog.csdn.net/weixin_45626288/article/details/119579104
        //查询商家账户余额够不够提现
        if (amount.compareTo(alipayFundAccountQuery()) < 0){
            //todo 商家余额不足异常
            throw new ToBeConstructed();
        }
        AlipayFundTransUniTransferModel model=new AlipayFundTransUniTransferModel();
        model.setOutBizNo(outBizNo);
        model.setTransAmount(amount.toString());
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderTitle("Gseek账户提现");

        Participant payee=new Participant();
        payee.setIdentityType("ALIPAY_LOGON_ID");
        payee.setIdentity(identity);
        model.setPayeeInfo(payee);

        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        request.setNotifyUrl(AlipayConfig.NOTIFY_URL);
        request.setReturnUrl(AlipayConfig.RETURN_URL);
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);

        if (response.isSuccess()){
            log.debug("提现成功");
        }else {
            //todo 处理一大堆支付宝传来的异常状态码
            String subCode=response.getSubCode();
            log.warn("提现失败，电脑网站支付状态码为:\n{}\n状态信息为:\n{}",subCode,response.getSubMsg());
            //todo 充值失败异常
            throw new ToBeConstructed();
        }

    }


    /**
     * alipay.fund.account.query: 支付宝查询商家余额.
     *
     * */
    public BigDecimal alipayFundAccountQuery()
            throws AlipayApiException, JsonProcessingException {
        AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
        model.setAlipayUserId(AlipayConfig.PID);
        model.setAccountType("ACCTRANS_ACCOUNT");
        AlipayFundAccountQueryRequest request=new AlipayFundAccountQueryRequest();
        request.setBizModel(model);

        AlipayFundAccountQueryResponse response= alipayClient.pageExecute(request);

        if (response.isSuccess()) {
            return new BigDecimal(response.getAvailableAmount());
        } else {
            //todo 处理一大堆支付宝传来的异常状态码
            String subCode=response.getSubCode();

            log.error("查询支付宝商家余额时出错，状态码为:\n{}\n状态信息为:\n{}",response.getSubCode(),response.getSubMsg());

            //todo 查询商家余额异常
            throw new ToBeConstructed();
        }
    }

}
