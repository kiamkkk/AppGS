package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.RepeatLoginException;
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
            //根据userName来构建token
            String userName = authenticationToken.getName();
            String token=TokenUtil.gainToken(userName);
            if (authenticationToken.getDetails() instanceof CustomWebAuthenticationDetails details){
                int userId =details.getUserId();
                redisService.saveToken(token,userName,""+userId);

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
