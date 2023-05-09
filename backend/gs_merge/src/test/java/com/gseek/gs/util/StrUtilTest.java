package com.gseek.gs.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Phak
 * @since 2023/5/5-20:17
 */
@SpringBootTest
public class StrUtilTest {

    @Test
    void test(){
        checkTokenWhenTokenRight("Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY4MzI4ODg3NCwiZXhwIjoxNjgzMjk2MDc0fQ.wcQoJtY_XC5VJTX3ZDGaBUjV3Q7arwHtENlOwFSljypgnixoHftxZ6QwfowaqXvv");
    }

    @Test
    public void checkTokenWhenTokenRight(String rightToken){
        assert StrUtil.checkToken(rightToken) : "rightToken|"+rightToken+"\nStrUtil fail";
    }
}
