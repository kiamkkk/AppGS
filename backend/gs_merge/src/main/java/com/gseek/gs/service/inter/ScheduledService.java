package com.gseek.gs.service.inter;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 注册定时方法.
 *
 * @author Phak
 * @since 2023/5/22-16:12
 */
public interface ScheduledService {

    @Async("async")
    @Scheduled(cron="0/1 * 0/1 * * ?")
    void redisToMysql();

}
