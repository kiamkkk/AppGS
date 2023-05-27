package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.TokenGrade;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetailsSource;
import com.gseek.gs.exce.business.login.TokenInvalidException;
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

            String token = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
            // token过期
            if (TokenUtil.isTokenExpired(token)){
                log.info("token过期");
                throw new TokenInvalidException("TokenInvalid");
            }

            log.info("token格式正确{}",token);
            TokenGrade grade=TokenUtil.extractTokenGrade(token);
            String userName = TokenUtil.extractClaim(token, Claims::getSubject);
            log.info("用户名{},权限为{}",userName,grade);

            if (! token.isBlank() && redisService.isUserHasToken(userName)) {
                if (! userName.isBlank() ) {
                    //验证token是否过期
                    int code=redisService.inspectToken(token);
                    switch (code) {
                        case TOKEN_VALID -> {
                            log.info("token可用|"+token);
                        }
                        case TOKEN_REISSUE -> {
                            String newToken = tokenUtil.reissueToken(token, userName, grade);
                            //todo 放响应头里感觉不太好，我是这样想：
                            // 1、要么直接在这放进响应体里，controller里再读一遍响应体来改；
                            // 2、要么想想办法把新token传到controller里，统一写入响应体。
                            response.setHeader("newToken", newToken);
                            log.info("token重签发|"+token);
                        }
                        case TOKEN_INVALID -> {
                            log.warn("token不可用|"+token);
                            throw new TokenInvalidException("TokenInvalid");
                        }
                        default -> {
                            log.error("检查token是否过期时出错：出现异常状态码|" + code);
                        }
                    }
                    log.info("状态码{}",code);

                    // todo 根据权限不同，使用不同的UserService
                    if (grade==TokenGrade.USER){
                        OrdinaryUser user =(OrdinaryUser) this.userService.loadUserByUsername(userName);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user, user.getPassword(), user.getAuthorities());
                        authentication.setDetails(new CustomWebAuthenticationDetailsSource().buildDetails(
                                request).setUserId(user.getUserId()));

                        log.info("认证用户" + userName + ",设置security context");

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }else if (grade==TokenGrade.ADMIN){
                        OrdinaryAdmin admin=(OrdinaryAdmin) this.adminService.loadUserByUsername(userName);
                        log.info(admin.toString());
                        UsernamePasswordAuthenticationToken adminAuthentication = new UsernamePasswordAuthenticationToken(
                                admin, admin.getPassword(), admin.getAuthorities());
                        adminAuthentication.setDetails(new AdminWebAuthenticationDetailsSource().buildDetails(
                                request).setAdminId(admin.getAdminId()));

                        log.info("认证管理员" + userName + ",设置security context");

                        SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
                    }else {
                        log.warn("权限错误");
                    }
                }

            }
        }else {
            log.info("token异常");
        }
        log.info("doFilterInternal结束");
        chain.doFilter(request, response);
    }
}
