package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.login.CustomAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证失败后的流程.
 * 根据异常返回响应.
 *
 * @author Phak
 * @since 2023/5/3-13:43
 */
@Slf4j
@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        log.info("登录时错误：{}",exception.getMessage());

        ObjectNode objectNode=objectMapper.createObjectNode();
        if (exception instanceof UsernameNotFoundException) {
            response.setStatus(403);
            objectNode.put("code", 403);
            objectNode.put("message", "UserNotFound");
        }
        if (exception instanceof BadCredentialsException) {
            response.setStatus(403);
            objectNode.put("code", 403);
            objectNode.put("message", "PasswordWrong");
        }
        if (exception instanceof CustomAuthenticationException cae){
            response.setStatus(403);
            objectNode.put("code", 403);
            objectNode.put("msg", cae.getMessage());
        }

        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(objectNode.toPrettyString());
        }
    }
}
