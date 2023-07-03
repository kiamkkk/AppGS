package com.gseek.gs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Phak
 * @since 2023/5/5-20:17
 */
@SpringBootTest
@Slf4j
public class StrUtilTest {

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void checkTokenWhenRight(){
        assert StrUtil.checkToken("Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJnc2Vla1VzZXIxIiwiZXhwIjoxNjg0NzY3MzgwfQ.JqtrpVdZvwlK2vzzbQX7A4Sb_v4c7eAJpzSkLZVF-UU2uwXHlwhgAKDEculxgU8l");
    }


    @Test
    public void checkTokenWhenWrong(){
        assert ! StrUtil.checkToken("Bear asdasdsa");
    }


    @Test
    public void checkUserNameWhenRight(){
        assert StrUtil.checkUserName("gseekUser1");
    }


    @Test
    public void checkUserNameWhenWrong(){
        assert ! StrUtil.checkUserName("Bear\s##user123nNAME爱我中华 ");
    }


    @Test
    public void checkEmailWhenRight(){
        assert StrUtil.checkEmail("12345678@sad.vom");

    }


    @Test
    public void checkEmailWhenWrong(){
        assert ! StrUtil.checkEmail("12345$678#sad.vom");
    }

    @Test
    public void checkDestinationWhenRight(){
        assert StrUtil.checkUserDestination("/user/114514/abcdefg/hijklmn")
                : "StrUtil#checkUserDestination fail";
    }


}
