package com.gseek.gs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝的设置内容注入在这里.
 * 
 * @author Phak
 * @since 2023/5/14-15:04
 */
@Configuration
@Slf4j
public class AlipayConfig {
    /**
     * 应用ID
     */
    public static String appId ;
    /**
     * 应用公匙
     * */

    public static String appPublicKey;
    /**
     * 商户私钥，PKCS8格式RSA2私钥
     */

    public static String merchantPrivateKey;
    /**
     * 支付宝公钥
     */

    public static String alipayPublicKey;
    /**
     * 接口加密密匙
     * */

    public static String encryptKey;
    /**
     * 商户id
     * */

    public static String pid;

    /**
     * 服务器异步通知页面路径
     */

    public static String notifyUrl;
    /**
     * 页面跳转同步通知页面路径
     */

    public static String returnUrl;
    /**
     * 用户付款中途退出返回商户网站的地址
     * */

    public static String quitUrl;

    /**
     * 签名方式
     */

    public static String signType;
    /**
     * 字符编码格式
     */

    public static String charset;
    /**
     * 支付宝网关
     */

    public static String gatewayUrl;
    /**
     * 支付过期时间
     * */

    public static String timeOut;

    @Value("${alipay.key.sandbox.app_id}")
    public void setAppId(String appId) {
        AlipayConfig.appId = appId;
    }
    @Value("${alipay.key.sandbox.merchant_public_key}")
    public void setAppPublicKey(String appPublicKey) {
        AlipayConfig.appPublicKey = appPublicKey;
    }
    @Value("${alipay.key.sandbox.merchant_private_key}")
    public void setMerchantPrivateKey(String merchantPrivateKey) {
        AlipayConfig.merchantPrivateKey = merchantPrivateKey;
    }
    @Value("${alipay.key.sandbox.alipay_public_key}")
    public void setAlipayPublicKey(String alipayPublicKey) {
        AlipayConfig.alipayPublicKey = alipayPublicKey;
    }
    @Value("${alipay.key.sandbox.encrypt_key}")
    public void setEncryptKey(String encryptKey) {
        AlipayConfig.encryptKey = encryptKey;
    }
    @Value("${alipay.key.sandbox.pid}")
    public void setPid(String pid) {
        AlipayConfig.pid = pid;
    }
    @Value("${alipay.api.notify_url}")
    public void setNotifyUrl(String notifyUrl) {
        AlipayConfig.notifyUrl = notifyUrl;
    }
    @Value("${alipay.api.return_url}")
    public void setReturnUrl(String returnUrl) {
        AlipayConfig.returnUrl = returnUrl;
    }
    @Value("${alipay.api.quit_url}")
    public void setQuitUrl(String quitUrl) {
        AlipayConfig.quitUrl = quitUrl;
    }
    @Value("${alipay.common.sign_type}")
    public void setSignType(String signType) {
        AlipayConfig.signType = signType;
    }
    @Value("${alipay.common.charset}")
    public void setCharset(String charset) {
        AlipayConfig.charset = charset;
    }
    @Value("${alipay.common.gatewayUrl}")
    public void setGatewayUrl(String gatewayUrl) {
        AlipayConfig.gatewayUrl = gatewayUrl;
    }
    @Value("${alipay.common.timeout}")
    public void setTimeOut(String timeOut) {
        AlipayConfig.timeOut = timeOut;
    }
}
