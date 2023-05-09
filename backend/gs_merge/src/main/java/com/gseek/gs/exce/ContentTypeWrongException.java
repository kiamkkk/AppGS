package com.gseek.gs.exce;

/**
 * @author Phak
 * @since 2023/5/3-14:38
 */
public class ContentTypeWrongException extends BaseException{

    public ContentTypeWrongException(String... info) {
        super(info);
        code=400;
        msg="ContentTypeWrong";
    }
}
