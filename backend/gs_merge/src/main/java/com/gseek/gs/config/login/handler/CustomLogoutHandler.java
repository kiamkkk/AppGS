package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.common.Token;
import com.gseek.gs.pojo.bean.LogoutBean;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登出.
 * 如果有token,将token失效
 * 没有token,则验证请求体中的身份信息.
 *
 * @author Phak
 * @since 2023/6/14-17:02
 */
@Slf4j
@Component
public class CustomLogoutHandler extends SecurityContextLogoutHandler {
    @Autowired
    @Lazy
    ObjectMapper objectMapper;
    @Autowired
    @Lazy
    Result result;
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    @Lazy
    RedisService redisService;
    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rawToken = request.getHeader("authorization");
        // 请求头有token，使该token失效
        if (rawToken != null && ! rawToken.isBlank()){
            try {
                String cookedToken = rawToken.substring(TokenUtil.TOKEN_PREFIX.length());
                Token token=new Token(cookedToken);

                redisService.deleteKey(token.getName());
            }catch (ExpiredJwtException eje){
                log.info("该token已过期");
            }
        }else {
            // 请求头没有token，验证用户
            LogoutBean bean = new LogoutBean();
            try {
                bean = objectMapper.readValue(request.getInputStream(), LogoutBean.class);
            } catch (IOException ioe) {
                // 留空
                // 如果既没有token也没有认证信息,identify()就返回false.
            }
            // 认证通过则删除token
            if ( identify(bean, request) ){
                redisService.deleteKey(bean.getUserName());
            }
        }


        super.logout(request, response, authentication);
    }

    /**
     * 对bean给定的用户名和密码进行认证.
     *
     * 如果给定的身份认证信息错误,在request中添加<code>wrongIdentify</code>属性:
     * - <code>userName</code>为用户名错误
     * - <code>password</code>为密码错误
     *
     * @return  认证通过返回true,否则返回false
     * */
    private boolean identify(LogoutBean bean, HttpServletRequest request){
        if ( bean.getUserName().isBlank() ){
            request.setAttribute("wrongIdentify", "userName");
            return false;
        }
        if ( bean.getPassword().isBlank() ){
            request.setAttribute("wrongIdentify", "password");
            return false;
        }

        try{
            UserDetails userDetails = userService.loadUserByUsername(bean.getUserName());
            if (! passwordEncoder.matches(bean.getPassword(), userDetails.getPassword()) ){
                request.setAttribute("wrongIdentify", "password");
                return false;
            }
        }catch (UsernameNotFoundException une){
            request.setAttribute("wrongIdentify", "userName");
            return false;
        }

        return true;
    }
}
