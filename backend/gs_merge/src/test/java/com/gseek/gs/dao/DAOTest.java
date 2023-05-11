package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:11
 */
@SpringBootTest
@Slf4j
public class DAOTest {

    @Autowired
    GoodMapper goodMapper;

    @Autowired
    GoodCoverPicMapper goodCoverPicMapper;

    @Test
    public void test(){
        int a=1;
        List<GoodBO> bos=goodMapper.selectGoodsByUserIdWithoutTypeTagId(a);
        for(GoodBO bo:bos){
            log.error(bo.getTypeTagName());
            log.error(bo.getAccount());
            log.error(bo.getTagNames().get(0));
            log.error(bo.getCoverPictures().get(0));
            log.error(bo.getDetailPictures().get(0));
            log.error("end");
        }
/*        List<String> stes=goodCoverPicMapper.selectCoversByGoodId(1);
        for (String str:stes){
            log.error(str);
        }*/
    }

}
