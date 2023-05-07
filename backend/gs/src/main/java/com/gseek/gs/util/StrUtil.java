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
     * token：Bearer +xxx
     * */
    private static final String TOKEN_FORMAT="^Bearer:\\S+";

    /**
     * base64格式：
     * <ul>
     * <li>字符串只可能包含A-Z，a-z，0-9，+，/，=字符</li>
     * <li>字符串长度是4的倍数</li>
     * <li>=只会出现在字符串最后，可能没有或者一个等号或者两个等号</li>
     * </ul>
     * */
    private static final String BASE64_FORMAT = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_FORMAT);
    private static final Pattern USER_NAME_PATTERN = Pattern.compile(USERNAME_FORMAT);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_FORMAT);
    private static final Pattern TOKEN_PATTEN =Pattern.compile(TOKEN_FORMAT);
    private static  final Pattern BASE64_PATTEN=Pattern.compile(BASE64_FORMAT);

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

    /**
    * 检验是否为base64格式
     *
     * @param str 待检验字符串
    * */
    public static boolean isBase64(String str) {
        Matcher matcher=BASE64_PATTEN.matcher(str);
        return matcher.matches();
    }

    /**
     * 根据给定字符串，生成encryptKey.
     *
     * @param str 字符串
     * @return
     */
    public static String gainKey(String str) {
        //todo 好像没什么用，再看看要不要删掉
        byte[] b = str.getBytes();
        return byteToHexString(b);
    }

    /**
     * byte数组转化为16进制字符串.
     *
     * @param bytes byte数组
     * @return 16进制字符串
     */
    public static String byteToHexString(byte[] bytes) {
        //todo 好像没什么用，再看看要不要删掉
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2) {
                    sb.append("0").append(strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

}
