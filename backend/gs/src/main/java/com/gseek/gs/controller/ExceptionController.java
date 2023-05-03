package com.gseek.gs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.exce.BaseException;
import com.gseek.gs.exce.ServerBackendException;
import com.gseek.gs.exce.business.ParameterWrong;
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

    @ExceptionHandler(ParameterWrong.class)
    public String parameterWrongHandler(ParameterWrong parameterWrong, HttpServletResponse response){
        log.debug("参数有误");
        response.setStatus(500);
        return packageMessage(parameterWrong);
    }

    @ExceptionHandler(ServerBackendException.class)
    public String serverBackendExceptionHandler(ServerBackendException serverBackendException, HttpServletResponse response){
        response.setStatus(500);
        return packageMessage(serverBackendException);
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
