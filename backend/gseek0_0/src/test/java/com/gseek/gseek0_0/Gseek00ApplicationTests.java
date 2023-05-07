package com.gseek.gseek0_0;

import com.gseek.gseek0_0.dao.UserMapper;

import com.gseek.gseek0_0.pojo.Blacklist;
import com.gseek.gseek0_0.service.BlacklistService;
import com.gseek.gseek0_0.service.UserService;
import com.gseek.gseek0_0.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@SpringBootTest
class Gseek00ApplicationTests {
    @Autowired
    BlacklistService blacklistService;
    @Test
    void contextLoads() throws SQLException {
        HashMap map=new HashMap();
        map.put("admin_id",6);
        map.put("appeal_result",false);
        map.put("disapprove_reason","disapproveReason");
        map.put("black_id",8);
        System.out.println(blacklistService.auditReport(map));

    }
}
