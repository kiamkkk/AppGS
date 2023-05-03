package com.gseek.gs.service.impl;

import com.gseek.gs.exce.business.RepeatLoginException;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Phak
 * @since 2023/5/3-13:57
 */
@Service("redisServiceImpl")
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    StringRedisTemplate template;

    //todo 最好不要用状态码
    /**
     * token有效
     * */
    public static final int TOKEN_VALID=1;
    /**
     * token无效
     * */
    public static final int TOKEN_INVALID=-1;
    /**
     * token有效但要重新签发
     * */
    public static final int TOKEN_REISSUE=2;
    
    private static final long IMMINENT_TIME=10*60*1000;

    @Override
    public void saveToken(String token,int userId) {
        if (isRepeatLogin(token)){
            throw new RepeatLoginException();
        }
        addKey(token, userId+"", TokenUtil.EFFECTIVE_TIME,TimeUnit.MILLISECONDS);
    }

    @Override
    public int inspectToken(String token) {
        if (token==null||token.isBlank()){
            log.info("token is NULL or empty");
            return TOKEN_INVALID;
        }
        if (isExist(token)){
            if (System.currentTimeMillis()>
                    tokenUtil.extractClaim(token, Claims::getExpiration).getTime()-IMMINENT_TIME){
                log.debug("token reissue");
                return TOKEN_REISSUE;
            }
            return TOKEN_VALID;
        }
        return TOKEN_INVALID;
    }

    @Override
    public boolean isRepeatLogin(String userName){

        return true;
    }

    /**
     * 储存String.
     *
     * @param key      储存的键值
     * @param value    储存的值
     * @param timeout  时间
     * @param timeUnit 时间单位
     */
    private void addKey(String key, String value, long timeout, TimeUnit timeUnit) {
        template.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获得储存的值.
     *
     * @param key
     * @return 储存键值对应的数据
     */
    private String getKey(String key) {
        ValueOperations<String, String> operation = template.opsForValue();
        return operation.get(key);
    }

    /**
     * 判断值是否存在.
     *
     * @param key
     * @return true为存在
     * */
    private boolean isExist(String key){
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    /**
     * 删除单个值.
     *
     * @param key
     */
    private boolean deleteKey(String key) {
        return Boolean.TRUE.equals(template.delete(key));
    }

   
}
