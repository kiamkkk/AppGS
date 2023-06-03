package com.gseek.gs.config.login.handler.admin;

import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.bean.OrdinaryAdmin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 在认证时向authentication中加入userId
 *
 * @author Isabella
 * @since 2023/5/17-15:59
 */
@Slf4j
public class AdminDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails admin) {
        log.info("createSuccessAdminAuthentication开始");//
        AbstractAuthenticationToken result= (AbstractAuthenticationToken) super.createSuccessAuthentication(principal,authentication,admin);
        //把adminId存进去，方便以后操作数据库
        if (result.getDetails() instanceof AdminWebAuthenticationDetails details){

            if (admin instanceof OrdinaryAdmin ordinaryAdmin){

                log.info("设置管理员id|"+ordinaryAdmin.getAdminId());
                details.setAdminId(ordinaryAdmin.getAdminId());
                log.info(details.toString());

            }else {
                log.error("向下转型失败|不能将user转为OrdinaryAdmin");
                throw new ServerException("登录时出错");
            }

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为AdminWebAuthenticationDetails");
            throw new ServerException("登录时出错");
        }
        log.info("createSuccessAdminAuthentication结束");//
        return result;
    }


}
