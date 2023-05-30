package com.gseek.gs;

import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.controller.BlacklistController;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@SpringBootTest
class GsApplicationTests {
    @Autowired
    BlacklistService blacklistService;
    @Test
    void contextLoads() {
        BlacklistDTO blacklistDTO=new BlacklistDTO(1,1,"fail","no");

        System.out.println( blacklistService.addReport(blacklistDTO));
    }

}
