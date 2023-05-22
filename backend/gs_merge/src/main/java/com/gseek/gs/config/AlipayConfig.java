package com.gseek.gs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Phak
 * @since 2023/5/14-15:04
 */
@Configuration
public class AlipayConfig {
    /**
     * 应用ID
     */

    public static String APP_ID ;
    /**
     * 应用公匙
     * */

    public static String APP_PUBLIC_KEY;
    /**
     * 商户私钥，PKCS8格式RSA2私钥
     */

    public static String MERCHANT_PRIVATE_KEY;
    /**
     * 支付宝公钥
     */

    public static String ALIPAY_PUBLIC_KEY;
    /**
     * 接口加密密匙
     * */

    public static String ENCRYPT_KEY;
    /**
     * 商户id
     * */

    public static String PID;

    /**
     * 服务器异步通知页面路径
     */

    public static String NOTIFY_URL;
    /**
     * 页面跳转同步通知页面路径
     */

    public static String RETURN_URL;
    /**
     * 用户付款中途退出返回商户网站的地址
     * */

    public static String QUIT_URL;

    /**
     * 签名方式
     */

    public static String SIGN_TYPE;
    /**
     * 字符编码格式
     */

    public static String CHARSET;
    /**
     * 支付宝网关
     */

    public static String GATEWAY_URL;
    /**
     * 支付过期时间
     * */

    public static String TIME_OUT;

    @Value("${alipay.key.sandbox.app_id}")
    public void setAppId(String appId) {
        APP_ID = appId;
    }
    @Value("${alipay.key.sandbox.merchant_public_key}")
    public void setAppPublicKey(String appPublicKey) {
        APP_PUBLIC_KEY = appPublicKey;
    }
    @Value("${alipay.key.sandbox.merchant_private_key}")
    public void setMerchantPrivateKey(String merchantPrivateKey) {
        MERCHANT_PRIVATE_KEY = merchantPrivateKey;
    }
    @Value("${alipay.key.sandbox.alipay_public_key}")
    public void setAlipayPublicKey(String alipayPublicKey) {
        ALIPAY_PUBLIC_KEY = alipayPublicKey;
    }
    @Value("${alipay.key.sandbox.encrypt_key}")
    public void setEncryptKey(String encryptKey) {
        ENCRYPT_KEY = encryptKey;
    }
    @Value("${alipay.key.sandbox.pid}")
    public void setPID(String PID) {
        AlipayConfig.PID = PID;
    }
    @Value("${alipay.api.notify_url}")
    public void setNotifyUrl(String notifyUrl) {
        NOTIFY_URL = notifyUrl;
    }
    @Value("${alipay.api.return_url}")
    public void setReturnUrl(String returnUrl) {
        RETURN_URL = returnUrl;
    }
    @Value("${alipay.api.quit_url}")
    public void setQuitUrl(String quitUrl) {
        QUIT_URL = quitUrl;
    }
    @Value("${alipay.common.sign_type}")
    public void setSignType(String signType) {
        SIGN_TYPE = signType;
    }
    @Value("${alipay.common.charset}")
    public void setCHARSET(String CHARSET) {
        AlipayConfig.CHARSET = CHARSET;
    }
    @Value("${alipay.common.gatewayUrl}")
    public void setGatewayUrl(String gatewayUrl) {
        GATEWAY_URL = gatewayUrl;
    }
    @Value("${alipay.common.timeout}")
    public void setTimeOut(String timeOut) {
        TIME_OUT = timeOut;
    }
}
