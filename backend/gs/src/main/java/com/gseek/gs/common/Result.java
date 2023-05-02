package com.gseek.gs.common;

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

    private ResultCode resultCode;

    private Result postSuccess;

    @Autowired
    ObjectMapper objectMapper;

    private Result(){};

    private Result(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String gainPostSuccess() throws JsonProcessingException {
        if (postSuccess == null){
            postSuccess = new Result(ResultCode.POST_SUCCESS);
        }
        return objectMapper.writeValueAsString(postSuccess);
    }

}
