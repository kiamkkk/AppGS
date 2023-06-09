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

    private int code;
    private String msg;
    @JsonIgnore
    private Result getSuccess;
    @JsonIgnore
    private Result postSuccess;
    @JsonIgnore
    private Result patchSuccess;
    @JsonIgnore
    private Result deleteSuccess;
    @JsonIgnore
    private Result putSuccess;

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

    public String gainGetSuccess() throws JsonProcessingException {
        if (getSuccess == null){
            getSuccess = new Result(ResultCode.GET_SUCCESS);
        }
        return objectMapper.writeValueAsString(getSuccess);
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
    public String gainDeleteSuccess() throws JsonProcessingException {
        if (deleteSuccess == null){
            deleteSuccess = new Result(ResultCode.DETECT_SUCCESS);
        }
        return objectMapper.writeValueAsString(deleteSuccess);
    }

    public String gainPutSuccess() throws JsonProcessingException {
        if (putSuccess == null){
            putSuccess = new Result(ResultCode.PUT_SUCCESS);
        }
        return objectMapper.writeValueAsString(putSuccess);
    }


}
