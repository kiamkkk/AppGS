package com.gseek.gs.service.inter;

import com.gseek.gs.exce.business.RepeatLoginException;

/**
 * @author Phak
 * @since 2023/5/3-13:56
 */
public interface RedisService {

    /**
     * 用户登录后储存token.
     * redis中过期时间为: token过期时间
     *
     * @param token token
     * */
    void saveToken(String token,String userName,String userId) throws RepeatLoginException;

    /**
     * 检验token是否有效:
     * 1.token不存在或为空,无效
     * 2.token存在,但有效时间小于DEVIATE_TIME,有效但重新签发
     * 3.token存在且有效时间大于DEVIATE_TIME,有效
     *
     * @param token
     * @return TOKEN_VALID    有效
     *         TOKEN_INVALID  无效
     *         TOKEN_REISSUE  重新签发
     * */
    //todo 最好不要用状态码
    int inspectToken(String token);

    /**
     * 检验库中是否已经有该用户的token
     *
     * @param userName 用户名
     * @return 有则返回true
     * */

    boolean isRepeatLogin(String token, String userName);

    boolean isUserHasToken(String token);

    /**
     * 获得储存的值.
     *
     * @param key
     * @return 储存键值对应的数据
     */
    String getKey(String key);

    /**
     * 删除单个值.
     *
     * @param key
     */
    boolean deleteKey(String key);

    boolean fuzzyQuery(String matchKey);
}
