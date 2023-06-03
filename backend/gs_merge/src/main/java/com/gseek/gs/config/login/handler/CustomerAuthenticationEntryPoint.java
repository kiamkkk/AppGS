package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.login.CustomAuthenticationException;
import com.gseek.gs.exce.login.RepeatLoginException;
import com.gseek.gs.exce.login.TokenInvalidException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理两个自定义的登录异常.
 *
 * @author Phak
 * @since 2023/5/22-19:30
 * @deprecated 自定义的登录异常不应该在这个位置处理，而是在CustomAuthenticationFailureHandler中
 */

@Slf4j
public class CustomerAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public CustomerAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("开始处理登录异常");

        if (authException instanceof CustomAuthenticationException){
            log.debug("开始处理自定义异常");

            response.setStatus(403);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("utf-8");
            ObjectMapper objectMapper=new ObjectMapper();
            ObjectNode objectNode=objectMapper.createObjectNode();
            objectNode.put("code", 403);
            if (authException instanceof TokenInvalidException tokenInvalidException){
                objectNode.put("msg", tokenInvalidException.getMessage());
            }
            if (authException instanceof RepeatLoginException repeatLoginException){
                objectNode.put("msg", repeatLoginException.getMessage());
            }
            try (PrintWriter printWriter = response.getWriter()){
                log.info(objectNode.toPrettyString());
                printWriter.write(objectNode.toPrettyString());
                printWriter.flush();
            }

            log.debug("结束处理自定义异常");
            return;
        }
        super.commence(request, response, authException);

        log.debug("结束处理登录异常");
    }

}
