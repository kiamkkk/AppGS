package com.gseek.gs.config.login.handler;

import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 在认证时向authentication中加入userId.
 *
 * @author Phak
 * @since 2023/5/5-1:09
 */
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        log.debug("createSuccessAuthentication开始");

        AbstractAuthenticationToken result= (AbstractAuthenticationToken)
                super.createSuccessAuthentication(principal,authentication,user);

        //把userId存进去，方便以后操作数据库
        if (result.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (user instanceof OrdinaryUser ordinaryUser){

                log.debug("设置用户id{}",ordinaryUser.getUserId());
                details.setUserId(ordinaryUser.getUserId());

            }else {
                log.error("向下转型失败:不能将user转为OrdinaryUser");
                throw new ServerException("登录时出错");
            }

        }else {
            log.error("向下转型失败:不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("登录时出错");
        }

        log.debug("createSuccessAuthentication结束");
        return result;
    }

}
