package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BoughtGoodBO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Phak
 * @since 2023/6/4-16:07
 */
@SpringBootTest
public class BoughtGoodMapperTest {

    @Autowired
    BoughtGoodMapper boughtGoodMapper;

    /**
     * @deprecated 数据库里还没有相关数据
     * */
    @Disabled
    @Test
    public void selectAllBoughtGoodWhenExist(){
        List<BoughtGoodBO> bos = boughtGoodMapper.selectBoughtGoodsByUserId(3);
        assert bos.size() >0 : "failure";
    }
}
