package com.gseek.gs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
    void test() throws JsonProcessingException {

        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        log.error(objectMapper.writeValueAsString(list));

    }

    @Test
    public void checkTokenWhenTokenRight(String rightToken){
        assert StrUtil.checkToken(rightToken) : "rightToken|"+rightToken+"\nStrUtil fail";
    }

    @Test
    public void userNameIsBlankWhenUserNameNotBlank(){
        assert ! "user1".isBlank() : "blank";
    }
}
