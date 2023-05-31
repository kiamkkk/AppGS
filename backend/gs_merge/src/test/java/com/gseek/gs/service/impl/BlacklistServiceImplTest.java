package com.gseek.gs.service.impl;

import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Phak
 * @since 2023/5/30-15:45
 */
@SpringBootTest
public class BlacklistServiceImplTest {

    @Autowired
    BlacklistService blacklistService;

    @Test
    public void insertReportWhenReportRight(){
        BlacklistDTO blacklistDTO=new BlacklistDTO(1,1,"fail","no");
        assert blacklistService.addReport(blacklistDTO) == 1 : "insert fail";
    }

}
