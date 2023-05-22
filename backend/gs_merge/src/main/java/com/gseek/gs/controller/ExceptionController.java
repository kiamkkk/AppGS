package com.gseek.gs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.BaseException;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.ToBeConstructed;
import com.gseek.gs.exce.business.BusinessException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.login.RepeatLoginException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 包装发送所有业务异常
 *
 * @author Phak
 * @since 2023/5/2-21:52
 */

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @Autowired
    ObjectMapper objectMapper;

    @ExceptionHandler({IOException.class, SQLException.class})
    public String unknownExceptionHandler(Exception e,HttpServletResponse response){
        log.info("unknownException: {}",e.getMessage());
        response.setStatus(500);
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("code", 50010);
        objectNode.put("message", "后端未知错误");
        log.error(e.getMessage());
        return objectNode.toPrettyString();
    }

    @ExceptionHandler(ToBeConstructed.class)
    public String toBeConstructedExceptionHandler(ToBeConstructed e,HttpServletResponse response){
        log.info("ToBeConstructed: {}",e.getMsg());
        response.setStatus(500);
        return "后端竟然还有没写的异常！！！带着以下消息火速前去拷打廖俊煜！！！\n"+e.getMessage();
    }

    @ExceptionHandler(BusinessException.class)
    public String businessExceptionHandel(BusinessException e,HttpServletResponse response){
        log.info("BusinessException: {}",e.getMsg());
        response.setStatus(e.getCode());
        return packageMessage(e);
    }

    @ExceptionHandler(ForbiddenException.class)
    public String forbiddenExceptionHandel(ForbiddenException e,HttpServletResponse response){
        log.info("ForbiddenException: {}",e.getMsg());
        response.setStatus(e.getCode());
        return packageMessage(e);
    }

    @ExceptionHandler(ServerException.class)
    public String serverExceptionHandler(ServerException e, HttpServletResponse response){
        log.info("ServerException: {}",e.getMsg());
        response.setStatus(500);
        return packageMessage(e);
    }

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
