package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作good_detail表.
 *
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface GoodDetailPicMapper {

    /**
     * 记录商品详情图片储存路径
     * 应该在插入新商品时使用.
     * */
    List<String> selectDetailsByGoodId(@Param("goodId") int goodId);
    /**
     * 通过商品id,获取商品详情图片储存路径.
     * */
    int insertDetailPic(@Param("goodId") int goodId,@Param("list") List<String> paths);
}
