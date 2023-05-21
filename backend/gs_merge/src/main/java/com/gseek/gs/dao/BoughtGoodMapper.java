package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BoughtGoodBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/12-11:14
 */
@Mapper
public interface BoughtGoodMapper {

    /**
     *
     * */
    List<BoughtGoodBO> selectBoughtGoodsByUserId(@Param("userId") int userId);

    /**
     *
     * */
    int insertBoughtGoods(@Param("goodId") int goodId, @Param("time") long time,@Param("buyerId") int buyerId);

    /**
     *
     * */
    int deleteBoughtGoods(@Param("goodId") int goodId);
}
