package com.gseek.gs.controller;

import com.gseek.gs.exce.business.ParameterWrong;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 包装发送所有业务异常
 *
 * @author Phak
 * @since 2023/5/2-21:52
 */

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ParameterWrong.class)
    public String parameterWrongHandler(ParameterWrong parameterWrong){


        return "啊对对对";
    }

}
