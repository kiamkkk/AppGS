package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.ResultCode;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 封装通用的几个方法.
 *
 * @author Phak
 * @since 2023/5/27-17:53
 *
 * @deprecated 好像没什么用...
 */
public interface Service {

    /**
     * 根据通用响应返回值，设置状态码.
     *
     * @param response controller对应的响应
     * @param resultCode  通用响应码
     * @param objectMapper 处理json
     *
     * @deprecated 也许应该用一个filter来实现这个
     * */
    default String responseByResult(
            ObjectMapper objectMapper, ResultCode resultCode, HttpServletResponse response
    ) throws JsonProcessingException {
        response.setStatus(resultCode.getCode());

        return objectMapper.writeValueAsString(resultCode);
    }

}
