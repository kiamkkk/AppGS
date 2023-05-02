package com.gseek.gs.exce.business;

import java.util.Arrays;

/**
 * 参数异常.
 *
 * @author Phak
 * @since 2023/5/2-20:55
 */
public class ParameterWrong extends BusinessException {

    /**
     * 参数错误构造函数
     * 状态码:400
     * @param info 错误参数名
     * */
    public ParameterWrong(String... info) {
        super(info);
        code=400;
        msg="ParameterWrong:check "+ Arrays.toString(info);
    }
}
