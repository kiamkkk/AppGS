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
    void saveToken(String token) throws RepeatLoginException;

    /**
     * 查看token是否存在
     *
     * @param token token
     * @return token存在则返回true
     * */
    boolean tokenExist(String token);
}
