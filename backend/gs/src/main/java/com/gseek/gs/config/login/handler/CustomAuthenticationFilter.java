package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.exce.ContentTypeWrongException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义认证流程.
 *
 * @author Phak
 * @since 2023/5/3-13:27
 */
public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException ,ContentTypeWrongException{

        //仅当ContentType为application/json时进行登录
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper mapper = new ObjectMapper();
            CustomAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                AuthenticationBean authenticationBean = mapper.readValue(is,AuthenticationBean.class);
                authRequest = new CustomAuthenticationToken(
                        authenticationBean.getUserName(), authenticationBean.getPassword(),0);
            }catch (IOException e) {
                e.printStackTrace();
                //todo -1表示不成功???好像有点不妥
                authRequest = new CustomAuthenticationToken(
                        "", "",-1);
            }finally {
                setDetails(request, authRequest);
            }

            return this.getAuthenticationManager().authenticate(authRequest);
        }
        //否则抛出异常
        else {
            throw new ContentTypeWrongException();
        }
    }
}
