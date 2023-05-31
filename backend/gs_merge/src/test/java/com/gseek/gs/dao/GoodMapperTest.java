package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodBO;
import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/31-0:14
 */
@SpringBootTest
@Slf4j
public class GoodMapperTest {

    @Autowired
    GoodMapper goodMapper;

    @Test
    public void selectGoodsWithoutAccountAndSoldBOWhenExist(){
        List<Integer> integers=new ArrayList<>();
        integers.add(1);
        List<GoodsWithoutAccountAndSoldBO> bos=goodMapper.selectGoodsByGoodIdsWithoutAccountAndSold(
                    integers
        );

        assert bos != null : "failure";
        log.info(bos.get(0).getGoodId().toString());
        log.info(bos.get(0).getTagNames().get(0));
        log.info(bos.get(0).getCoverPictures().get(0));
        log.info(bos.get(0).getDetailPictures().get(0));
    }

    @Test
    public void selectGoodsByUserIdWithoutTypeTagIdWhenExist(){
        List<GoodBO> bos=goodMapper.selectGoodsByUserIdWithoutTypeTagId(
                6
        );

        assert bos != null : "failure";
        log.info(bos.get(0).getGoodId().toString());
        log.info(bos.get(0).getTagNames().get(0));
        log.info(bos.get(0).getCoverPictures().get(0));
        log.info(bos.get(0).getDetailPictures().get(0));
    }


}
