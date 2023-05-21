package com.gseek.gs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 密码工具类.
 * 1| 获取盐,
 * 2| 根据aes来加密、解密.
 *
 * @author Phak
 * @since 2023/5/2-21:47
 */
@Component
@Slf4j
public class PasswordUtil {
    @Value("${custom.encryptKey}")
    private String encryptKey;
    
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法.算法名称/加密模式/数据填充方式
     * */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static SecretKeySpec keySpec;

    private static Cipher cipher;

    /**
     * 初始化加密器.
     * key值放在yml里,用spring注入进来.
     * */
    @PostConstruct
    private void init(){
        // 设置加密秘钥
        setSecretKey();
        // 设置密码器
        setCipher();
    }

    /**
     *  随机获取一个长度64盐
     *
     * @return salt 盐
     * */
    public static String gainSalt(){
        char[] chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                "1234567890").toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     * 加密.
     *
     * @param toEncrypt 待加密字段
     * @return result Base64转码后加密结果
     * @throws IllegalBlockSizeException 加密时出错
     * @throws BadPaddingException 加密时出错
     * */
    public static String encrypt(String toEncrypt)
            throws IllegalBlockSizeException, BadPaddingException {
        // 设置为UTF-8编码
        final byte[] byteContent = toEncrypt.getBytes(StandardCharsets.UTF_8);
        // 加密
        final byte[] result = cipher.doFinal(byteContent);
        // 通过Base64转码返回
        return Base64.encodeBase64String(result);
    }

    /**
     * 解密.
     *
     * @param toDecrypt 待加密字段,格式为base64
     * @return result 解密结果,格式为UTF-8
     * */
    public static String decrypt(String toDecrypt)
            throws BadPaddingException, JsonProcessingException, IllegalBlockSizeException {
        try {
            // 判断是否为base64格式
            if (StrUtil.checkBase64(toDecrypt)){
                throw new ParameterWrongException(
                        new ParameterWrongBean()
                                .addParameters("待加密字段",toDecrypt)
                );
            }
            // 从base64转码后解密
            final byte[] result = cipher.doFinal(Base64.decodeBase64(toDecrypt));
            // 采用UTF-8编码转化为字符串
            return new String(result, StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error("加密时出错：\n"+e.getMessage(),e);
            throw e;
        }
    }

    /**
     * 设置密码器
     * */
    private void setCipher(){
        try {
            // 创建密码器
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } catch (Exception e) {
            log.error("密码器初始化错误：\n"+e.getMessage(), e);
        }
    }

    /**
     * 设置加密秘钥
     */
    private void setSecretKey() {
        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(encryptKey.getBytes()));
            // 生成一个密钥
            final SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            keySpec = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (final NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
    }
    
}
