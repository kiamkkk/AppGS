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
    @Value("${alipay.key.sandbox.app_id}")
    public static String APP_ID ;
    /**
     * 应用公匙
     * */
    @Value("${alipay.key.sandbox.merchant_public_key}")
    public static String APP_PUBLIC_KEY;
    /**
     * 商户私钥，PKCS8格式RSA2私钥
     */
    @Value("${alipay.key.sandbox.merchant_private_key}")
    public static String MERCHANT_PRIVATE_KEY;
    /**
     * 支付宝公钥
     */
    @Value("${alipay.key.sandbox.alipay_public_key}")
    public static String ALIPAY_PUBLIC_KEY;
    /**
     * 接口加密密匙
     * */
    @Value("${alipay.key.sandbox.encrypt_key}")
    public static String ENCRYPT_KEY;
    /**
     * 商户id
     * */
    @Value("${alipay.key.sandbox.pid}")
    public static String PID;

    /**
     * 服务器异步通知页面路径
     */
    @Value("${alipay.api.notify_url}")
    public static String NOTIFY_URL;
    /**
     * 页面跳转同步通知页面路径
     */
    @Value("${alipay.api.return_url}")
    public static String RETURN_URL;
    /**
     * 用户付款中途退出返回商户网站的地址
     * */
    @Value("${alipay.api.quit_url}")
    public static String QUIT_URL;

    /**
     * 签名方式
     */
    @Value("${alipay.common.sign_type}")
    public static String SIGN_TYPE = "RSA2";
    /**
     * 字符编码格式
     */
    @Value("${alipay.common.charset}")
    public static String CHARSET = "utf-8";
    /**
     * 支付宝网关
     */
    @Value("${alipay.common.gatewayUrl}")
    public static String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    /**
     * 支付过期时间
     * */
    @Value("${alipay.common.timeout}")
    public static String TIME_OUT;
}
