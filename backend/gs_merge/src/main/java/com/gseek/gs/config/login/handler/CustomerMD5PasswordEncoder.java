package com.gseek.gs.config.login.handler;

import com.gseek.gs.service.inter.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;

/**
 * 采用MD5进行加密，且在比较密码的时候向待验密码加入自己生成的盐.
 *
 * @author Phak
 * @since 2023/5/22-0:05
 */
public class CustomerMD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return encode((String) rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(
                    encode(
                            addSalt(rawPassword,encodedPassword)
                    )
        );
    }

    private String addSalt(CharSequence rawPassword, String encodedPassword){
        String salt=encodedPassword.substring(encodedPassword.indexOf(UserService.PREFIX),encodedPassword.indexOf(UserService.SUFFIX)+1);
        String passwordWithSalt =salt+rawPassword;
        return passwordWithSalt;
    }

    /**
     * 使用MD5对原文进行加密
     * */
    private String encode(String rawPassword) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = rawPassword.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
