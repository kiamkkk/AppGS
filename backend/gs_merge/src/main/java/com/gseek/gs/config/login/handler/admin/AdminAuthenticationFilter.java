package com.gseek.gs.config.login.handler.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.login.ContentTypeWrongException;
import com.gseek.gs.pojo.bean.AuthenticationBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 自定义认证流程.将登陆接口设为/admin
 *
 * @author Isabella
 * @since 2023/5/17-13:30
 */
@Slf4j
public class AdminAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 将登陆接口设为 /admin
     * */
    public AdminAuthenticationFilter() {
        super(new AntPathRequestMatcher("/admin","POST"));
    }

    public AdminAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/admin","POST"), authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("adminAttemptAuthentication开始");//
        //仅当ContentType为application/json时进行登录
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            //从请求体中读出userName和password
            try (InputStream is = request.getInputStream()){
                AuthenticationBean authenticationBean = mapper.readValue(is,AuthenticationBean.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.getAdminName(), authenticationBean.getAdminPassword());
                setDetails(request, authRequest);
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            }
            log.info("adminAttemptAuthentication结束");//
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        //否则抛出异常
        else {
            throw new ContentTypeWrongException();
        }

    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
