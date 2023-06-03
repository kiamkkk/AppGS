package com.gseek.gs.controller;

import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;


/**
 * Controller通用方法.
 *
 * @author Phak
 * @since 2023/6/3-23:49
 * @deprecated 在鉴权流程完善后不会出现Authentication为null的问题,没必要保留这个类.
 */
public interface Controller {
    Logger logger = LoggerFactory.getLogger(Controller.class);

    /**
     * 我原本想把DTOPerService#perService()放在这里的,但又想起这个接口有可能被废弃,所以就没放进来.
     * @deprecated 好像没什么用....
     * */
    default CustomWebAuthenticationDetails perService(Authentication authentication)
        throws ServerException {
        return checkAuthentication(authentication);
    }


    /**
     * 对Authentication中的details进行转型,捕捉异常.
     * 调试过程中出现太多因为authentication为null导致的问题了......比起在每个controller的每个方法里都来个try-catch,
     * 我觉得统一调用这个方法更美观一点.
     *
     * @return details 如果转型没问题就返回CustomWebAuthenticationDetails
     *
     * @deprecated 在鉴权流程完善后不会出现Authentication为null的问题,没必要保留这个方法.
     * */
    default CustomWebAuthenticationDetails checkAuthentication(Authentication authentication)
            throws ServerException{
        // authentication不为null
        if (authentication == null){
            logger.error("authentication为null");
            throw new ServerException("认证时出错", new NullPointerException("authentication为null"));
        }

        CustomWebAuthenticationDetails details;

        try {
            details = (CustomWebAuthenticationDetails)authentication.getDetails();
        } catch (ClassCastException cce ){
            logger.error("向下转型失败:authentication中的detail是 {} 类的,不能转为CustomWebAuthenticationDetails",
                    authentication.getDetails().getClass().getName());
            throw new ServerException("认证时出错", cce);
        } catch (RuntimeException e){
            logger.error("向下转型失败:未知错误 \n {}", e.getMessage());
            throw new ServerException("认证时出错", e);
        }

        return details;
    }

}
