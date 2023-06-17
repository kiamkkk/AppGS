package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.login.ContentTypeWrongException;
import com.gseek.gs.pojo.bean.AuthenticationBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义认证流程.将普通用户登陆接口设为/users
 *
 * @author Phak
 * @since 2023/5/3-13:27
 */
@Slf4j
public class CustomAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {

    /**
     * 将登陆接口设为 /users
     * */
    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/users","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("attemptAuthentication开始");

        //仅当ContentType为application/json时进行登录
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            //从请求体中读出userName和password
            try (InputStream is = request.getInputStream()){
                AuthenticationBean authenticationBean = mapper.readValue(is,AuthenticationBean.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.getUserName(), authenticationBean.getPassword());
                setDetails(request, authRequest);
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "[null]", "[null]");
            }

            log.debug("attemptAuthentication结束");
            return this.getAuthenticationManager().authenticate(authRequest);
        }
        // ContentType异常
        else {
            log.info("attemptAuthentication 抛出异常 ContentTypeWrongException");
            throw new ContentTypeWrongException();
        }

    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
