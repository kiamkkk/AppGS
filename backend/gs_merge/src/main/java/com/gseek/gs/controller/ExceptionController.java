package com.gseek.gs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.BaseException;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.ToBeConstructed;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.exce.business.RepeatLoginException;
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
        response.setStatus(500);
        ObjectNode objectNode=objectMapper.createObjectNode();
        objectNode.put("code", 50010);
        objectNode.put("message", "后端未知错误");
        return objectNode.toPrettyString();
    }

    @ExceptionHandler(ParameterWrongException.class)
    public String parameterWrongHandler(ParameterWrongException parameterWrong, HttpServletResponse response){
        log.debug("参数有误");
        response.setStatus(500);
        return packageMessage(parameterWrong);
    }

    @ExceptionHandler(ServerException.class)
    public String serverExceptionHandler(ServerException ServerException, HttpServletResponse response){
        response.setStatus(500);
        return packageMessage(ServerException);
    }

    @ExceptionHandler(RepeatLoginException.class)
    public String repeatLoginExceptionHandler(RepeatLoginException e,HttpServletResponse response){
        response.setStatus(403);
        return packageMessage(e);
    }

    @ExceptionHandler(ToBeConstructed.class)
    public String toBeConstructedExceptionHandler(ToBeConstructed e,HttpServletResponse response){
        response.setStatus(500);
        return "后端竟然还有没写的异常！！！带着以下消息火速前去拷打廖俊煜！！！\n"+e.getMessage();
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
