package com.gseek.gs.config.login.handler.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.business.login.RepeatLoginException;
import com.gseek.gs.exce.business.login.TokenInvalidException;
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
 *
 * @author Isabella
 * @since 2023/5/17-13:33
 */
@Slf4j
@Component("adminAuthenticationFailureHandler")
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        log.info("登录时错误：{}",exception.getMessage());
        ObjectNode objectNode=objectMapper.createObjectNode();
        if (exception instanceof UsernameNotFoundException) {
            response.setStatus(400);
            objectNode.put("code", 400);
            objectNode.put("message", "UsernameNotFound");
        }
        if (exception instanceof BadCredentialsException) {
            response.setStatus(400);
            objectNode.put("code", 400);
            objectNode.put("message", "PasswordWrong");
        }
        if (exception instanceof TokenInvalidException tokenInvalidException){
            response.setStatus(403);
            objectNode.put("code", 403);
            objectNode.put("msg", tokenInvalidException.getMessage());
        }
        if (exception instanceof RepeatLoginException repeatLoginException){
            response.setStatus(403);
            objectNode.put("code", 403);
            objectNode.put("msg", repeatLoginException.getMessage());
        }

        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(objectNode.toPrettyString());
        }
    }
}
