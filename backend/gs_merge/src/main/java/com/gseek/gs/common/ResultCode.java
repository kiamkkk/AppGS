package com.gseek.gs.common;

import lombok.Getter;


/**
 * 状态码枚举.
 *
 * @author Phak
 * @since 2023/5/2-21:19
 */
@Getter
public enum ResultCode {
    /**
     * get成功默认响应
     * */
    GET_SUCCESS(200,"success"),
    /**
     * post成功默认响应
     * */
    POST_SUCCESS(201,"success"),
    /**
     * patch成功默认响应
     * */
    PATCH_SUCCESS(201,"success"),
    /**
     * detect成功默认响应
     * */
    DETECT_SUCCESS(201,"success"),
    /**
     * put成功默认响应
     * */
    PUT_SUCCESS(201,"success"),
    /**
     * 服务器后端内部错误
     * */
    INTERNAL_SERVER_BACKEND_ERROR(50001,"INTERNAL_SERVER_ERROR"),
    /**
     * 用户被拉黑
     * */
    User_BLACKLISTED(403,"USER_BLACKLISTED");


    /**
     * 状态码
     * */
    private int code;
    /**
     * 状态信息
     * */
    private String msg;

    private ResultCode(int code,String msg){
        this.code=code;
        this.msg=msg;
    }



}
