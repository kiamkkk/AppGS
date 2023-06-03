package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.common.TokenGrade;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.login.RepeatLoginException;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证成功后的流程.
 * 登录成功后签发token.
 *
 * @author Phak
 * @since 2023/5/3-13:42
 */
@Component("customAuthenticationSuccessHandler")
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws RepeatLoginException, ServerException, IOException {
        log.debug("onAuthenticationSuccess开始");

        // 根据username来构建token
        // 不根据userId来构建token,因为JwtAuthenticationTokenFilter要根据username获取用户登录信息
        // String userId =details.getUserId()+"";
        String username =authentication.getName();
        // 创建保存token
        String token=TokenUtil.gainToken(username, TokenGrade.USER);
        redisService.saveToken(token, username);
        // 返回token
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("token", token);
        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(objectNode.toPrettyString());
        }

        log.debug("onAuthenticationSuccess结束");
    }
}
