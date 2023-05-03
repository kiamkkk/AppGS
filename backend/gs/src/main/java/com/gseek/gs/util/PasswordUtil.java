package com.gseek.gs.util;

import org.springframework.stereotype.Component;

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
public class PasswordUtil {


    /**
     *  随机获取一个长度64盐
     *
     * @return salt 盐
     * */
    public String gainSalt(){
        char[] chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                "1234567890!@#$%^&*()_+").toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            //Random().nextInt()返回值为[0,n)
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     * 根据aes进行解密
     *
     * @param toDecode 待解密字段
     * @return source 原字段
     * */
    public String decode(String toDecode){
        String source=null;
        //todo 实现解密

       return source;
    }

    /**
     * 根据aes进行加密
     *
     * @param toEncode 待加密字段
     * @return result 加密字段
     * */
    public String encode(String toEncode){
        String result = null;
        //todo 实现加密

        return result;
    }

}
