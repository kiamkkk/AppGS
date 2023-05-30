package com.gseek.gs.config.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
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
 * 确定用户是否在黑名单内 并 拒绝黑名单用户的部分操作.
 * 黑名单用户只能访问：
 * <code>[get]/users/account</code>
 * <code>[post]/users/account</code>
 * <code>[patch]/users/account</code>
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
        // 访问路径为/users/account则不进行判断
        String path=request.getContextPath();
        if (Objects.equal("/users/account", path) ){
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int userId=details.getUserId();
            Boolean bo=blacklistService.queryByRespondentId(userId);
            if(bo==null){
                log.debug("用户{}没有被举报过",userId);
                return true;
            }

            if (Boolean.TRUE.equals(bo)){
                // 用户被拉入黑名单，禁止访问该项目
                log.info("用户{}已经被加入黑名单，禁止访问{}", details.getUserId(), path);
                throw new ForbiddenException("用户已经被加入黑名单，禁止访问");
            }else {
                log.debug("用户{}还不是黑名单用户",userId);
            }
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
        return true;
    }
}
