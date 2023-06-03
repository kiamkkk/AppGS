package com.gseek.gs.exce;

/**
 * 占位用的异常.
 *
 * @author Phak
 * @since 2023/5/21-23:15
 *
 * @deprecated 这个异常只在开发时标记要抛出异常的位置,必须被规范为其他确定的异常.
 */
public class ToBeConstructed extends BaseException{
    public ToBeConstructed(String message) {
        super(message);
        code=500;
    }

}
