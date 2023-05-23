package com.gseek.gs.exce;

/**
 * 自定义基异常.
 * 所有自定义异常都要直接或间接地继承自该类.
 *
 * @author Phak
 * @since 2023/5/2-20:38
 */

public class BaseException extends RuntimeException {
    protected int code;
    protected String msg;


    public BaseException() {
        super();
    }

    public BaseException(String... info) {
        super();
    }

    public BaseException(Throwable cause,String... info) {
        super(cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause,
                         boolean enableSuppression, boolean writableStackTrace,
                         String... info) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
