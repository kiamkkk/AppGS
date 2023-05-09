package com.gseek.gs.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 一般响应类.
 *
 * @author Phak
 * @since 2023/5/2-21:18
 */
@Getter
@Setter
@Component
public class Result {
    //todo 要不要统一响应???
    private int code;
    private String msg;
    private Result postSuccess;
    private Result patchSuccess;

    @Autowired
    @JsonIgnore
    ObjectMapper objectMapper;

    private Result(){};

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(ResultCode resultCode) {
        this.code=resultCode.getCode();
        this.msg=resultCode.getMsg();
    }

    public String gainPostSuccess() throws JsonProcessingException {
        if (postSuccess == null){
            postSuccess = new Result(ResultCode.POST_SUCCESS);
        }
        return objectMapper.writeValueAsString(postSuccess);
    }

    public String gainPatchSuccess() throws JsonProcessingException {
        if (patchSuccess == null){
            patchSuccess = new Result(ResultCode.PATCH_SUCCESS);
        }
        return objectMapper.writeValueAsString(patchSuccess);
    }

}
