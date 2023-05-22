package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.login.RepeatLoginException;
import com.gseek.gs.service.inter.RedisService;
import com.gseek.gs.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义认证成功后的流程.
 *
 * @author Phak
 * @since 2023/5/3-13:42
 */
@Component
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
            throws RepeatLoginException,IOException,ServerException {
        log.debug("onAuthenticationSuccess开始");
        if (authentication instanceof UsernamePasswordAuthenticationToken authenticationToken){
            if (authenticationToken.getDetails() instanceof CustomWebAuthenticationDetails details){
                // 根据username来构建token
                // 不根据userId来构建token,因为JwtAuthenticationTokenFilter要根据username获取用户登录信息
                String username =authentication.getName();
                String userId =details.getUserId()+"";
                String token=TokenUtil.gainToken(username);
                redisService.saveToken(token, username);

                ObjectNode objectNode=objectMapper.createObjectNode();
                objectNode.put("token", token);
                try (PrintWriter printWriter = response.getWriter()){
                    printWriter.write(objectNode.toPrettyString());
                }
            }else {
                log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
                throw new ServerException("登录时出错");
            }

        }else {
            log.error("向下转型失败:登录时无法将Authentication转为UsernamePasswordAuthenticationToken");
            throw new ServerException("登录时出错");
        }
        log.debug("onAuthenticationSuccess结束");
    }
}
