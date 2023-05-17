package com.gseek.gs.config.login.handler.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@Component("adminAuthenticationFailureHandler")
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        ObjectNode objectNode=objectMapper.createObjectNode();
        if (exception instanceof UsernameNotFoundException) {
            response.setStatus(400);
            objectNode.put("code", 400);
            objectNode.put("message", "AdminNameNotFound");
        }
        if (exception instanceof BadCredentialsException) {
            response.setStatus(400);
            objectNode.put("code", 400);
            objectNode.put("message", "PasswordWrong");
        }

        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(objectNode.toPrettyString());
        }
    }
}
