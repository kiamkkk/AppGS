package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetailsSource;
import com.gseek.gs.exce.business.TokenInvalidException;
import com.gseek.gs.pojo.bean.OrdinaryAdmin;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import com.gseek.gs.service.inter.AdminService;
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
    @Autowired
    @Qualifier("adminServiceImpl")
    AdminService adminService;

    @SuppressWarnings("AlibabaSwitchStatement")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String rawToken = request.getHeader("Authorization");
        log.info("doFilterInternal开始");

        if (rawToken==null){
            log.info("该请求无token，直接放行");
            chain.doFilter(request, response);
            return;
        }
        if (StrUtil.checkToken(rawToken)) {
            final String token = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
            log.info("token格式正确|"+token);
            String username = TokenUtil.extractClaim(token, Claims::getSubject);
            log.info("用户名|"+username);
            if (! token.isBlank() && redisService.isUserHasToken(username)) {
                if (! username.isBlank() ) {
                    //验证token是否过期
                    int code=redisService.inspectToken(token);
                    switch (code) {
                        case TOKEN_VALID -> {
                            log.info("token可用|"+token);
                        }
                        case TOKEN_REISSUE -> {
                            String newToken = tokenUtil.reissueToken(token, username);
                            //todo 放响应头里感觉不太好，我是这样想：
                            // 1、要么直接在这放进响应体里，controller里再读一遍响应体来改；
                            // 2、要么想想办法把新token传到controller里，统一写入响应体。
                            response.setHeader("newToken", newToken);
                            log.info("token重签发|"+token);
                        }
                        case TOKEN_INVALID -> {
                            log.warn("token不可用|"+token);
                            throw new TokenInvalidException();
                        }
                        default -> {
                            log.error("检查token是否过期时出错：出现异常状态码|" + code);
                        }
                    }
                    log.info("状态码|"+code);

                    OrdinaryUser user =(OrdinaryUser) this.userService.loadUserByUsername(username);
                    log.info(user.toString());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, user.getPassword(), user.getAuthorities());
                    authentication.setDetails(new CustomWebAuthenticationDetailsSource().buildDetails(
                            request).setUserId(user.getUserId()));

                    log.info("认证用户" + username + ",设置security context");

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    /** update by Isabella**/
                    OrdinaryAdmin admin=(OrdinaryAdmin) this.adminService.loadUserByUsername(username);
                    log.info(admin.toString());
                    UsernamePasswordAuthenticationToken adminAuthentication = new UsernamePasswordAuthenticationToken(
                            admin, admin.getPassword(), user.getAuthorities());
                    adminAuthentication.setDetails(new AdminWebAuthenticationDetailsSource().buildDetails(
                            request).setAdminId(admin.getAdminId()));

                    log.info("认证管理员" + username + ",设置security context");

                    SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
                    /** update by Isabella end **/


                }


            }
        }else {
            log.info("token异常");
        }
        log.info("doFilterInternal结束");
        chain.doFilter(request, response);
    }
}
