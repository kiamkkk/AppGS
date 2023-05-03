package com.gseek.gseek0_0;

import com.gseek.gseek0_0.dao.UserMapper;
import com.gseek.gseek0_0.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class Gseek00ApplicationTests {
    @Autowired
    UserService userService;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(userService.getUserPassword());

    }
}
