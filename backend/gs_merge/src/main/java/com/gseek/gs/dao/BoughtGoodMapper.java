package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BoughtGoodBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作bought_good表
 *
 * @author Phak
 * @since 2023/5/12-11:14
 */
@Mapper
public interface BoughtGoodMapper {

    /**
     * 插入购买商品信息.
     * 买家付款后进行.
     * */
    int insertBoughtGoods(@Param("goodId") int goodId, @Param("time") long time,@Param("buyerId") int buyerId);

    /**
     * 删除购买商品信息.
     * 买家付款后,交易取消或交易异常时进行.
     * 物理删除.
     * */
    int deleteBoughtGoods(@Param("goodId") int goodId);

    /**
     * 根据用户名,获取所有用户购买过的商品.
     * */
    List<BoughtGoodBO> selectBoughtGoodsByUserId(@Param("userId") int userId);
}
