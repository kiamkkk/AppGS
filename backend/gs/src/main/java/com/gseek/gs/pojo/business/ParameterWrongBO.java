package com.gseek.gs.pojo.business;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 规范错误参数的内部传递.
 * 调用addParameters添加参数.
 *
 * @author Phak
 * @since 2023/5/7-17:18
 */
@Component()
public class ParameterWrongBO {

    private Map<String,String> wrongParameters;


    public ParameterWrongBO() {
        wrongParameters=new HashMap<>(16);
    }

    /**
     * 添加错误参数名与参数值.
     *
     * @param paraName 参数名
     * @param paraValue 参数值
     * @return 本身,方便连续调用
     *  */
    public ParameterWrongBO addParameters(String paraName,String paraValue){
        //todo 想想看用什么格式，把这两个值塞进wrongParameters里

        return this;
    }

    public Map<String, String> getWrongParameters() {
        return wrongParameters;
    }
}
