package com.gseek.gs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.BaseException;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.ToBeConstructed;
import com.gseek.gs.exce.business.BusinessException;
import com.gseek.gs.exce.login.RepeatLoginException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 包装发送所有异常.
 * springSecurity中的异常不会在这里处理.
 *
 * @author Phak
 * @since 2023/5/2-21:52
 */

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 处理业务异常.
     * 这种异常的原因是用户给出的数据不正确,根据异常中的code与msg进行响应.
     * */
    @ExceptionHandler(BusinessException.class)
    public String businessExceptionHandel(BusinessException e,HttpServletResponse response){
        log.info("BusinessException: {}",e.getMsg());
        response.setStatus(e.getCode());
        return packageMessage(e);
    }

    /**
     * 处理服务器异常.
     * 这种异常的原因是服务器自身原因,不能通过用户重新给出数据解决.
     * */
    @ExceptionHandler(ServerException.class)
    public String serverExceptionHandler(ServerException e, HttpServletResponse response){
        log.error("ServerException:\n",e);
        response.setStatus(500);
        return packageMessage(e);
    }

    /**
     * 这个异常由userService抛出
     * */
    @ExceptionHandler(UsernameNotFoundException.class)
    public String usernameNotFoundExceptionHandel(BusinessException e,HttpServletResponse response){
        log.info("UsernameNotFoundException: {}",e.getMsg());
        response.setStatus(e.getCode());
        return "UserNotFound";
    }

    /**
     * 重复登录异常.
     * 这个异常在用户在持有一个有效token的时候进行登录时抛出.只会由RedisService#saveToken抛出.
     * */
    @ExceptionHandler(RepeatLoginException.class)
    public String repeatLoginExceptionHandel(RepeatLoginException e, HttpServletResponse response){
        log.info("ServerException: {}",e.getMessage());
        response.setStatus(403);

        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("code", 403);
        objectNode.put("message", e.getMessage());
        return objectNode.toPrettyString();
    }

    /**
     * 处理没有被包装过的异常.
     * 这种异常不应该出现,应当被规范化为ServerException或BusinessException.
     * */
    @ExceptionHandler({IOException.class, SQLException.class})
    public String unknownExceptionHandler(Exception e,HttpServletResponse response){
        log.warn("unknownException: {}",e.getMessage());
        response.setStatus(500);

        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("code", 50010);
        objectNode.put("message", "后端未知错误");

        return objectNode.toPrettyString();
    }
    /**
     * 处理尚未定义的异常.
     * 这种异常不应该出现.
     * */
    @ExceptionHandler(ToBeConstructed.class)
    public String toBeConstructedExceptionHandler(ToBeConstructed e,HttpServletResponse response){
        log.warn("ToBeConstructed: {}",e.getMsg());
        response.setStatus(500);
        return "后端竟然还有没写好的异常！！！带着以下消息火速前去拷打廖俊煜！！！\n"+e.getMessage();
    }


    /**
     * 以json包装自定义异常
     *
     * @param e 自定义异常
     * @return json格式中包含状态码、状态信息
     * */
    private String packageMessage(BaseException e) {
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("code", e.getCode());
        objectNode.put("message", e.getMsg());
        return objectNode.toPrettyString();
    }

}
