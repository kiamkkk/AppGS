package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodFavBO;
import com.gseek.gs.pojo.data.GoodFavDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作good_fav表.
 *
 * @author Phak
 * @since 2023/5/9-13:00
 */
@Mapper
public interface GoodFavMapper {
    /**
     * 插入收藏商品记录.
     * */
    int insertFav(@Param("userId") int userId,@Param("goodId") int goodId,@Param("time") long time);
    /**
     * 删除商品收藏记录.
     * 物理删除.
     * */
    int deleteFav(@Param("userId") int userId,@Param("goodId") int goodId);
    /**
     * 获取所有收藏商品信息.
     */
    List<GoodFavBO> selectAllFavByUserId(@Param("userId") int userId);

    /**
     * 根据用户id和商品id,获取单个收藏商品信息.
     * */
    GoodFavDO selectFavByUserIdAndGoodId(@Param("userId") int userId, @Param("goodId") int goodId);
}
