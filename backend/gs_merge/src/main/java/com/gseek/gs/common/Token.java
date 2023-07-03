package com.gseek.gs.common;

import com.gseek.gs.service.impl.RedisServiceImpl;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Phak
 * @since 2023/6/4-23:28
 */
@Slf4j
public class Token {
    private Claims claims;

    public Token(String token) {
        claims = TokenUtil.extractAllClaims(token);
    }

    /**
     * 去掉prefix后解析token
     * */
    public Token(String token, String prefix) {
        this(
                token.substring(prefix.length())
        );
    }

    /**
     * true为过期.
     * */
    public boolean checkExpired(){
        if ( claims.size() == 0 ){
            return true;
        }
        return System.currentTimeMillis() >= claims.getExpiration().getTime();
    }

    public String getName(){
        return TokenUtil.extractClaim(claims,  Claims::getSubject);
    }

    public TokenGrade getGrade(){
        return TokenUtil.extractTokenGrade(claims);
    }

    public boolean needReissue() {
        return System.currentTimeMillis() >
                claims.getExpiration().getTime()- RedisServiceImpl.IMMINENT_TIME;
    }


}
