package com.gseek.gs.util;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理字符串工具类
 *
 * @author Phak
 * @since 2023/5/2-20:51
 */
@Component
public class StrUtil {

    /**
     * 密码格式：只有英文大小写、数字
     * */
    private static final String PASSWORD_FORMAT="[0-9][a-z][A-Z]";
    /**
     * 用户名格式：只有英文大小写、中文、数字
     * */
    private static final String USERNAME_FORMAT="[0-9][a-z][A-Z][]\\u4e00-\\u9fa5]";
    /**
     * 邮箱格式：xxx@xxx.xxx
     * */
    private static final String EMAIL_FORMAT="\\\\w+@\\\\w+\\\\.a-z+(\\\\.a-z+)?";
    /**
     * tokengs：Bearer +xxx
     * */
    private static final String TOKEN_FORMAT="^Bearer:\\S+";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_FORMAT);
    private static final Pattern USER_NAME_PATTERN = Pattern.compile(USERNAME_FORMAT);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_FORMAT);
    private static final Pattern TOKEN_PATTEN =Pattern.compile(TOKEN_FORMAT);

    /**
     * 检验密码格式是否正确
     *
     * @param password 密码
     * */
    public static boolean checkPassword(final String password){
        Matcher matcher=PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    /**
     * 检验用户名格式是否正确
     *
     * @param userName 用户名
     * */
    public static boolean checkUserName(final String userName){
        Matcher matcher=USER_NAME_PATTERN.matcher(userName);
        return matcher.matches();
    }

    /**
     * 检验邮箱格式是否正确.
     *
     * @param email 邮箱
     * */
    public static boolean checkEmail(final String email){
        Matcher matcher=EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * 检验token格式.
     *
     * @param token
     * */
    public static boolean checkToken(final String token){
        Matcher matcher=TOKEN_PATTEN.matcher(token);
        return matcher.matches();
    }
}
