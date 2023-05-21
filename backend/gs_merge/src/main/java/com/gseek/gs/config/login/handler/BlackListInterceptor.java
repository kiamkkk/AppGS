package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.ResultCode;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.service.inter.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 *
 * @author Phak
 * @since 2023/5/20-20:10
 */
@Component
@Slf4j
public class BlackListInterceptor implements HandlerInterceptor {

    @Autowired
    @Qualifier("blacklistServiceImpl")
    BlacklistService blacklistService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int userId=details.getUserId();
            BlacklistResultBO bo=blacklistService.queryByRespondentId(userId);
            if (bo != null ){
                if (Boolean.TRUE==bo.isAppeal_result()){
                    ResultCode.User_BLACKLISTED;
                    //todo 用户被拉入黑名单，禁止访问该项目
                    throw new ForbiddenException("用户已经被加入黑名单，禁止访问");
                    return false;
                }
            }else {
                log.debug("用户 {} 没有被举报过",userId);
            }
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
        return true;
    }
}
