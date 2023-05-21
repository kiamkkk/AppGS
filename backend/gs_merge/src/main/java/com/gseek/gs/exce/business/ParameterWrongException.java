package com.gseek.gs.exce.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.pojo.bean.ParameterWrongBean;

/**
 * 参数异常.
 *
 * @author Phak
 * @since 2023/5/2-20:55
 */
public class ParameterWrongException extends BusinessException {

    public static ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 根据ParameterWrongBO构造错误信息.
     * 状态码:400
     *
     * @param bo 包含参数名与参数值
     * */
    public ParameterWrongException(ParameterWrongBean bo)
            throws JsonProcessingException {
        super();
        msg=bo.wrapWrongParameters(objectMapper);
        code=400;
    }
}
