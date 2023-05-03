package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.exce.business.ParameterWrong;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * 对应 /users/** 下的操作
 *
 * @author Phak
 * @since 2023/5/2-19:00
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    PasswordUtil  passwordUtil;

    @Autowired
    Result result;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody String json)
            throws JsonProcessingException,ParameterWrong, SQLException {

        JsonNode jsonNode=objectMapper.readTree(json);
        String userName=jsonNode.get("userName").asText();
        String password=passwordUtil.decode(jsonNode.get("password").asText());
        log.debug("解密后密码|"+password);
        String email=jsonNode.get("email").asText();
        long time=jsonNode.get("time").asLong();

        userService.register(userName,password,email,time);

        return result.gainPostSuccess();
    }



}
