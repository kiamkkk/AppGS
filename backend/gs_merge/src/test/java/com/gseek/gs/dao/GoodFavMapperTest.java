package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodFavBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Phak
 * @since 2023/6/4-16:09
 */
@SpringBootTest
@Slf4j
public class GoodFavMapperTest {

    @Autowired
    GoodFavMapper goodFavMapper;

    @Test
    public void selectAllFavWhenExist(){
        List<GoodFavBO> bos = goodFavMapper.selectAllFavByUserId(3);
        assert bos.size() >0 : "failure";
        log.warn(bos.get(0).toString());
    }
}
