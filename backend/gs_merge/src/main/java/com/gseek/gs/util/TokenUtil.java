package com.gseek.gs.util;

import com.gseek.gs.common.TokenGrade;
import com.gseek.gs.service.impl.RedisServiceImpl;
import com.gseek.gs.service.inter.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.JwtMap;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 生成、校验token
 *
 * @author Phak
 * @since 2023/5/3-13:47
 */
@Component
public class TokenUtil extends JwtMap {
    /**
     * token默认有效时间2小时
     * */
    public static final long EFFECTIVE_TIME = 2*60*60*1000;
    /**
     * token前缀
     * */
    public static final String TOKEN_PREFIX="Bearer ";



    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;

    //todo 应该使用随机生成的盐，不要偷懒！
    /**
     *
     * 密匙盐，来自https://allkeysgenerator.com/(Encryption key 256bit)
     * */
    private static final String SALT="586E3272357538782F413F4428472B4B6250645367566B597033733676397924";

    /**
     * 获取token
     * */
    public static String gainToken(String username, TokenGrade grade){
        assert (grade==TokenGrade.ADMIN || grade==TokenGrade.USER) :"grade必须为USER或ADMIN";
        return createToken(username, grade);
    }

    /**
     * 构建token：以"Bearer "开头，包含用户名、过期时间。
     *
     * @param userName 用户名
     * @return token
     * */
    private static String createToken(String userName, TokenGrade grade){
        //签发时间
        long issuedAt =System.currentTimeMillis();
        //用户名与权限等级
        Map<String,String> map=new HashMap<>(1);
        map.put(CustomClaims.TOKEN_GRADE, grade.name());

        return TOKEN_PREFIX+ Jwts.builder()
                .setClaims(map)
                .setSubject(userName)
                .setExpiration(new Date(issuedAt+EFFECTIVE_TIME)).
                signWith(getSignInKey()).compact();
    }

    /**
     * 验证token是否过期.
     *
     * @param token
     * @return true为过期
     */
    public static boolean isTokenExpired(String token) {
        return System.currentTimeMillis() >
                extractClaim(token, Claims::getExpiration).getTime();
    }

    public static boolean needReissue(String token) {
        return System.currentTimeMillis() >
                extractClaim(token, Claims::getExpiration).getTime()- RedisServiceImpl.IMMINENT_TIME;
    }

    /**
     * 解析token字符串中的信息.
     *
     * @param token
     * @return
     */
    public static <T> T extractClaim(String token, Function< Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static TokenGrade extractTokenGrade(String token){
        Claims claims = extractAllClaims(token);
        String grade=claims.get(CustomClaims.TOKEN_GRADE, String.class);
        return TokenGrade.gainTokenGradeByName(grade);
    }

    /**
     * 根据给定用户名重新签发token.
     * 删除旧token,储存并返回新token
     *
     * @param oldToken 旧token
//todo 补充
     * @return newToken 新token
     * */
    public String reissueToken(String oldToken, String userName, TokenGrade grade){
        redisService.deleteKey(userName);
        String newToken= gainToken(userName,grade);
        redisService.saveToken(newToken, userName);
        return newToken;
    }

    /**
     * 解析token字符串中的加密信息【加密算法&加密密钥】,提取所有声明的方法.
     *
     * @param token
     * @return claims
     */
    private static Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                // 获取alg开头的信息
                .setSigningKey(getSignInKey())
                .build()
                // 解析token字符串
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥的方法.
     *
     * @return 基于指定的密钥字节数组创建用于HMAC-SHA算法的新SecretKey实例
     */
    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SALT);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
