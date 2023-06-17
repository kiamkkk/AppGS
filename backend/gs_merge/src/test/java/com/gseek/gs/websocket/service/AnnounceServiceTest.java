package com.gseek.gs.websocket.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author Phak
 * @since 2023/6/4-13:33
 */
@SpringBootTest
public class AnnounceServiceTest {

    @Autowired
    AnnounceService announceService;

    @Disabled
    @Test
    public void readAnnounceWhenExist() throws IOException {
        announceService.readAnnounce();
        assert AnnounceService.announces.size() > 0 : "读取失败";
        System.err.println(AnnounceService.announces.get(AnnounceService.ANNOUNCE_PATH+"1.json"));
    }

}
