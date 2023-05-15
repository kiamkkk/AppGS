package com.gseek.gs.exce.business;

import com.gseek.gs.pojo.business.ParameterWrongBean;

import java.util.Arrays;

/**
 * 参数异常.
 *
 * @author Phak
 * @since 2023/5/2-20:55
 */
public class ParameterWrongException extends BusinessException {

    /**
     * 参数错误构造函数.
     * 状态码:400
     *
     * @param info 错误参数名|参数值+\n
     * */
    public ParameterWrongException(String... info) {
        super(info);
        code=400;
        msg="ParameterWrong:check "+ Arrays.toString(info);
    }

    /**
     * 根据ParameterWrongBO构造错误信息.
     * 状态码:400
     *
     * @param bo 包含参数名与参数值
     * */
    public ParameterWrongException(ParameterWrongBean bo){
        //todo 规范化错误信息显示
        super();
        code=400;
    }

}
