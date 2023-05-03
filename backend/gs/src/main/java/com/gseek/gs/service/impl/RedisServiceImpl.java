package com.gseek.gs.service.impl;

import com.gseek.gs.exce.business.RepeatLoginException;
import com.gseek.gs.service.inter.RedisService;
import org.springframework.stereotype.Service;

/**
 * @author Phak
 * @since 2023/5/3-13:57
 */
@Service("redisServiceImpl")
public class RedisServiceImpl implements RedisService {

    @Override
    public void saveToken(String token) {
        if (false){
            throw new RepeatLoginException();
        }
    }

    @Override
    public boolean tokenExist(String token) {

        return true;
    }
}
