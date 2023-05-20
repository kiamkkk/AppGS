package com.gseek.gs.util;

import com.gseek.gs.service.inter.RedisService;
import lombok.extern.slf4j.Slf4j;
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

}

