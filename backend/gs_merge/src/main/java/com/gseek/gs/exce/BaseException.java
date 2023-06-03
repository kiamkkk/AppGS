package com.gseek.gs.exce;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义基异常.
 * 所有自定义异常都要直接或间接地继承自该类,除了登录时发生的异常.
 * 登录时发生的异常要继承自CustomAuthenticationException.
 *
 * @author Phak
 * @since 2023/5/2-20:38
 */
@Getter
@Setter
public class BaseException extends RuntimeException {

    /**
     * 响应头中状态码.
     * - 这个状态码在ExceptionController中设置响应头时使用.
     * - 由于项目早期规划的缺陷,code和subCode往往是同一个值.
     * */
    protected int code;
    /**
     * 响应体中错误码.
     * - 这个状态码在ExceptionController中写入响应体时使用.
     * - 同code的区别: code是http状态码,不能自定义,表示大致的错误类型; subCode是自定义的,表示详细的错误类型.
     * - subCode的格式: code+两位整数,并在<code>ResultCode</code>中记录对应的含义.
     * @deprecated 由于项目早期规划的缺陷,code和subCode往往是同一个值.但我还是保留这个字段,希望有空能把这两个分开来.
     * */
    protected int subCode;
    /**
     * 响应体里的错误信息.
     * 在ExceptionController中写入响应体时使用.
     * */
    protected String msg;

    /**
     * @deprecated 异常中应当包含异常信息.
     * */
    public BaseException() {
        super();
    }

    /**
     * @deprecated 最好有对异常的描述
     * */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 自定义异常一般要用这个构造方法,因为业务中出现的问题原因往往是我们自己给出的.
     *
     * @param message 错误信息
     * */
    public BaseException(String message) {
        super(message);
        this.msg=message;
    }

    /**
     * 自定义异常也可以用用这个构造方法,因为也有可能借助原有的异常来表达业务逻辑里出现的问题.
     *
     * @param message 错误信息
     * @param cause 原始错误
     * */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.msg=message;
    }

}
