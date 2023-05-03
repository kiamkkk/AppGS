package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.ServerBackendException;
import com.gseek.gs.exce.business.RepeatLoginException;
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
            throws RepeatLoginException,IOException,ServerBackendException {
        response.setContentType("application/json;charset=utf-8");

        if (authentication instanceof CustomAuthenticationToken){
            CustomAuthenticationToken customAuthenticationToken=(CustomAuthenticationToken) authentication;
            //根据userName来构建token
            String userName = (String) customAuthenticationToken.getPrincipal();
            String token=TokenUtil.gainToken(userName);
            int userId=customAuthenticationToken.getUserId();
            redisService.saveToken(token,userId);

            ObjectNode objectNode=objectMapper.createObjectNode();
            objectNode.put("token", token);
            try (PrintWriter printWriter = response.getWriter()){
                printWriter.write(objectNode.toPrettyString());
            }catch (IOException e){
                throw e;
            }

        }else {
            log.error("向下转型失败:登录时无法将Authentication转为CustomAuthenticationToken");
            throw new ServerBackendException("登录时出错");
        }
    }
}
