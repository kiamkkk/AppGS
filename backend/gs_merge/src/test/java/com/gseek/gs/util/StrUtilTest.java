package com.gseek.gs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
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

    @Disabled
    @Test
    public void checkTokenWhenRight(){
        assert StrUtil.checkToken("Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY4MzU5NjY5OCwiZXhwIjoxNjgzNjAzODk4fQ.AFwbJHKjfV6rtilr388MHJL8zMldx6tIn98RUecLstLHC4p-m2tH0E-1fx8-H_di");
    }

    @Disabled
    @Test
    public void checkTokenWhenWrong(){
        assert ! StrUtil.checkToken("Bear asdasdsa");
    }

    @Disabled
    @Test
    public void checkUserNameWhenRight(){
        assert StrUtil.checkUserName("user123nNAME爱我中华");

    }

    @Disabled
    @Test
    public void checkUserNameWhenWrong(){
        assert ! StrUtil.checkUserName("Bear\s##user123nNAME爱我中华 ");
    }

    @Disabled
    @Test
    public void checkEmailWhenRight(){
        assert StrUtil.checkEmail("12345678@sad.vom");

    }

    @Disabled
    @Test
    public void checkEmailWhenWrong(){
        assert ! StrUtil.checkEmail("12345$678#sad.vom");
    }

    @Disabled
    @Test
    public void desensitizeIdNumberTest(){
        System.err.println(StrUtil.desensitizeIdNumber("37282420041306611x"));

    }

}
