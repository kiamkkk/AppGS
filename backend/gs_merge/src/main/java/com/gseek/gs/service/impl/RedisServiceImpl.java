package com.gseek.gs.service.impl;

import com.gseek.gs.exce.business.login.RepeatLoginException;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Phak
 * @since 2023/5/3-13:57
 */
@Service("redisServiceImpl")
@Slf4j
public class RedisServiceImpl implements RedisService {

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
    public void saveToken(String token,String userId) {
        if (isRepeatLogin(token,userId)){
            log.warn("重复登录|"+token);
            throw new RepeatLoginException();
        }
        addKey(userId, token, TokenUtil.EFFECTIVE_TIME,TimeUnit.MILLISECONDS);
    }

    @Override
    public int inspectToken(String token) {
        if (token==null||token.isBlank()){
            log.info("token is NULL or empty");
            return TOKEN_INVALID;
        }

        if (System.currentTimeMillis()>
                TokenUtil.extractClaim(token, Claims::getExpiration).getTime()-IMMINENT_TIME){
            log.debug("token reissue");
            return TOKEN_REISSUE;
        }

        return TOKEN_VALID;

    }

    @Override
    public boolean isRepeatLogin(String token, String userId){
        if (token.isBlank()){
            return false;
        }
        if (userId.isBlank()){
            return false;
        }
        String localToken=getKey(userId);
        if (localToken==null){
            return false;
        }
        return !localToken.equals(token);
    }
    @Override
    public boolean isUserHasToken(String userName){
        return isExist(userName);
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

    @Override
    public String getKey(String key) {
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
        Boolean a=template.hasKey(key);
        return Boolean.TRUE.equals(a);
    }

    @Override
    public boolean deleteKey(String key) {
        return Boolean.TRUE.equals(template.delete(key));
    }

    @Override
    public boolean fuzzyQuery(String matchKey){
        Set<String> set=template.keys("*"+matchKey+"*");
        // 将set转成ArrayList
        List<String> list=new ArrayList<>(set);
        if(list.size()!=0){
            for (String str:list){
                //通过查到的key值获取value，并放入result
                if (template.opsForValue().get(str)!=null){
                    return false;
                }
            }
        }
        return true;

    }

}
