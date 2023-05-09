package com.gseek.gs.util;

import com.gseek.gs.service.inter.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Phak
 * @since 2023/5/9-9:23
 */
@SpringBootTest
@Slf4j
public class RedisTest {

    @Autowired
    @Qualifier("redisServiceImpl")
    RedisService redisService;

    @Test
    public void isTokenExistWhenTokenExist() {
        assert ! redisService.isUserHasToken("Bearer:eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY4MzU5NjY5OCwiZXhwIjoxNjgzNjAzODk4fQ.AFwbJHKjfV6rtilr388MHJL8zMldx6tIn98RUecLstLHC4p-m2tH0E-1fx8-H_di") : "token not exist";
        log.info("token exist");
    }

    @Test
    public void fuzzyQueryWhenMatchKeyExist() {
        assert ! redisService.fuzzyQuery(
                "eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY4MzU5NjEzOCwiZXhwIjoxNjgzNjAzMzM4fQ"
        ) : "key exist" ;
    }

    @Test
    public void fuzzyQueryWhenMatchKeyNotExist() {
        assert ! redisService.fuzzyQuery(
                "eyJzdWIiOiJ1c2VyMS6MTY4MzU5NjEzOCwiZXhwIjoxNjgzNjAzMzM4fQ"
        ) : "key exist" ;
    }

    @Test
    public void fuzzyQueryWhenMatchKeyBlank() {
        assert ! redisService.fuzzyQuery(
                ""
        ) : "key exist" ;
    }

}

