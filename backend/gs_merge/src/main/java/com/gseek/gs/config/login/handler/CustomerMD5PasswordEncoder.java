package com.gseek.gs.config.login.handler;

import com.gseek.gs.service.inter.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.util.Objects;

/**
 * 采用MD5进行加密，且在比较密码的时候向待验密码加入自己生成的盐.
 *
 * @author Phak
 * @since 2023/5/22-0:05
 */
public class CustomerMD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return digest((String) rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(
                digest(
                        addSalt(rawPassword,encodedPassword)
                ),
                digest(encodedPassword)
        );
    }

    /**
     * 把正确密码前的盐加到待验证密码前
     *
     * @param encodedPassword 正确密码,密码前有盐,格式为<code>[盐]密码</code>
     * @param rawPassword 待验证密码
     *
     * @return 加盐后的待验证密码,格式为<code>[盐]密码</code>
     * */
    private String addSalt(CharSequence rawPassword, String encodedPassword){
        // 确认encodedPassword中有正确的盐
        if (encodedPassword.contains(UserService.PREFIX) && encodedPassword.contains(UserService.SUFFIX) ) {
            int prefix=encodedPassword.indexOf(UserService.PREFIX);
            int suffix=encodedPassword.indexOf(UserService.SUFFIX)+1;
            if (prefix < suffix){
                String salt=encodedPassword.substring(prefix, suffix);
                return salt+rawPassword;
            }
        }
        return (String) rawPassword;
    }

    /**
     * 使用MD5对原文进行加密
     *
     * @param rawPassword 待加密原文
     * */
    private String digest(String rawPassword) {
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
