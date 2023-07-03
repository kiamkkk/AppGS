package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Token;
import com.gseek.gs.common.TokenGrade;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetailsSource;
import com.gseek.gs.exce.login.TokenInvalidException;
import com.gseek.gs.pojo.bean.OrdinaryAdmin;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.StrUtil;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
// todo 为什么把这个类注册在ExceptionTranslationFilter后面才能正常处理TokenInvalidException异常???
/**
 * 根据token进行鉴权.
 * <ul>token在用户登录时签发,包含:用户名、过期时间、用户权限.</ul>
 *
 * <ul>根据token的过期时间不同分三种状态:</ul>
 * <ol>已过期,拒绝进一步访问,抛出TokenInvalidException异常</ol>
 * <ol>没过期但有效时间小于IMMINENT_TIME,将重新签发的token放在响应头的newToken字段中,允许访问</ol>
 * <ol>没过期且有效时间大于IMMINENT_TIME,允许访问</ol>
 *
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
    @Lazy
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;
    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;
    @Autowired
    @Qualifier("adminServiceImpl")
    @Lazy
    AdminService adminService;

    String logoutPath="/users/logout";

    @SuppressWarnings("AlibabaSwitchStatement")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getContextPath();
        String rawToken = request.getHeader("Authorization");
        log.debug("doFilterInternal开始");

        //todo websocket部分测试用，测完删掉！
        if (rawToken==null){
            rawToken = request.getParameter("Authentication");
        }
        //todo 测试部分结束

        if (rawToken==null){
            log.debug("该请求无token，直接放行");
            chain.doFilter(request, response);
            return;
        }else if(Objects.equals(logoutPath, path)){
            // 跳过登出请求
            chain.doFilter(request, response);
            return;
        }

        if (StrUtil.checkToken(rawToken)){
            // 去掉前缀后的token
            String cookedToken = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
            try {
                Token token=new Token(cookedToken);

                log.debug("token格式正确 {} ",cookedToken);
                TokenGrade grade=token.getGrade();
                String userName = token.getName();
                log.info("用户名{},权限为{}",userName,grade);

                if (! redisService.isUserHasToken(userName)) {
                    // 本地没有该token
                    throw new TokenInvalidException();
                }
                // token重签发
                if ( token.needReissue() ){
                    String newToken = tokenUtil.reissueToken(cookedToken, userName, grade);
                    //todo 放响应头里感觉不太好，我是这样想：
                    // 1、要么直接在这放进响应体里，controller里再读一遍响应体来改；
                    // 2、要么想想办法把新token传到controller里，统一写入响应体。
                    response.setHeader("newToken", newToken);
                    log.info("用户 {} 的token重签发,新token为 {}", userName, newToken);
                }

                // 根据权限不同，使用不同的UserService
                if (grade==TokenGrade.USER){
                    OrdinaryUser user =(OrdinaryUser) this.userService.loadUserByUsername(userName);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, user.getPassword(), user.getAuthorities());
                    authentication.setDetails(new CustomWebAuthenticationDetailsSource().buildDetails(
                            request).setUserId(user.getUserId()));

                    log.debug("认证用户 {} ,设置security context", userName);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else if (grade==TokenGrade.ADMIN){

                    OrdinaryAdmin admin=(OrdinaryAdmin) this.adminService.loadUserByUsername(userName);
                    log.info(admin.toString());
                    UsernamePasswordAuthenticationToken adminAuthentication = new UsernamePasswordAuthenticationToken(
                            admin, admin.getPassword(), admin.getAuthorities());
                    adminAuthentication.setDetails(new AdminWebAuthenticationDetailsSource().buildDetails(
                            request).setAdminId(admin.getAdminId()));

                    log.debug("认证管理员 {} ,设置security context", userName);

                    SecurityContextHolder.getContext().setAuthentication(adminAuthentication);
                }else {
                    log.warn("权限错误");
                }

            }catch (ExpiredJwtException eje){
                throw new TokenInvalidException();
            }
        }else {
            log.info("token {} 格式异常", rawToken);
        }

        log.debug("doFilterInternal结束");
        chain.doFilter(request, response);
    }
}
