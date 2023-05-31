package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.AdminDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Phak
 * @since 2023/5/25-18:45
 */
@SpringBootTest
@Slf4j
public class AdminMapperTest {

    @Autowired
    AdminMapper adminMapper;

    @Test
    public void selectAdminWhenAdminExist(){
        AdminDO adminDO=adminMapper.selectAdminByAdminName("admin1");
        assert adminDO != null :"查不出管理员账号: admin1" ;
    }
}
