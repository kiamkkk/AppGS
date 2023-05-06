package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.business.TokenInvalidException;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.StrUtil;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.gseek.gs.service.impl.RedisServiceImpl.*;

/**
 * @author Phak
 * @since 2023/5/4-21:55
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter  {

    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @SuppressWarnings("AlibabaSwitchStatement")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String rawToken = request.getHeader("Authorization");
        log.debug("doFilterInternal开始");

        if (rawToken==null){
            log.debug("该请求无token，直接放行");
            chain.doFilter(request, response);
            return;
        }
        if (StrUtil.checkToken(rawToken)) {
            final String token = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
            //todo redisService.isTokenExist(token)始终为false
            if (! token.isBlank() && redisService.isTokenExist(token)) {
                String username = TokenUtil.extractClaim(token, Claims::getSubject);
                if (username.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null ) {
                    //验证token是否过期
                    int code=redisService.inspectToken(token);
                    switch (code) {
                        case TOKEN_VALID -> {}
                        case TOKEN_REISSUE -> {
                            String newToken = tokenUtil.reissueToken(token, username);
                            //todo 放响应头里感觉不太好，我是这样想：
                            // 1、要么直接在这放进响应体里，controller里再读一遍响应体来改；
                            // 2、要么想想办法把新token传到controller里，统一写入响应体。
                            response.setHeader("newToken", token);
                        }
                        case TOKEN_INVALID -> {
                            throw new TokenInvalidException();
                        }
                        default -> {
                            log.error("检查token是否过期时出错：出现异常状态码|" + code);
                        }
                    }
                    log.debug("状态码|"+code);

                    UserDetails userDetails = this.userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new CustomWebAuthenticationDetailsSource().buildDetails(
                            request));

                    log.info("authenticated user " + username + ", setting security context");

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }else {
            log.info("token异常");
        }
        log.debug("doFilterInternal结束");
        chain.doFilter(request, response);
    }
}
