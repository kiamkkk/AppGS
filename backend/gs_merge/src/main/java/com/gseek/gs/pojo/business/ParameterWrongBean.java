package com.gseek.gs.pojo.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 规范错误参数的内部传递.
 * 调用addParameters添加参数.
 *
 * @author Phak
 * @since 2023/5/7-17:18
 */
public class ParameterWrongBean {

    private Map<String,String> wrongParameters;


    public ParameterWrongBean() {
        wrongParameters=new HashMap<>(16);
    }

    /**
     * 添加错误参数名与参数值.
     *
     * @param paraName 参数名
     * @param paraValue 参数值
     * @return 本身,方便连续调用
     *  */
    public ParameterWrongBean addParameters(String paraName,String paraValue){
        wrongParameters.put(paraName, paraValue);
        return this;
    }

    public Map<String, String> getWrongParameters() {
        return wrongParameters;
    }

    public String wrapWrongParameters(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(wrongParameters);
    }
}
