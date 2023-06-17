package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登出后处理.
 *
 * @author Phak
 * @since 2023/6/14-15:33
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    @Lazy
    Result result;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String identifyStatus = (String) request.getAttribute("wrongIdentify");
        if( identifyStatus == null){
            // 成功响应
            try (PrintWriter printWriter=response.getWriter()) {
                printWriter.write(result.gainPostSuccess());
            }
        }else {
            // 认证失败响应
            ObjectNode objectNode=objectMapper.createObjectNode();

            if ("userName".equals(identifyStatus)){
                response.setStatus(404);
                objectNode.put("code", 404);
                objectNode.put("message", "UserNotFound");
            } else if ("password".equals(identifyStatus)) {
                response.setStatus(403);
                objectNode.put("code", 403);
                objectNode.put("message", "PasswordWrong");
            }

            try (PrintWriter printWriter=response.getWriter()) {
                printWriter.write(objectNode.toPrettyString());
            }
        }
    }
}
