package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import com.gseek.gs.pojo.data.GoodDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodMapper {

    //todo 补充注释

    /**
     * 查出所有信息
     *
     *
     *
     * */
    GoodDO selectGoodByGoodIdFully(int goodId);

    /**
     *
     * */
    GoodsWithoutAccountAndSoldBO selectGoodByGoodIdWithoutAccount(@Param("goodId") int goodId);

    /**
     *
     * */
    int insertGood(@Param("goodDO") GoodDO goodDO);
}
