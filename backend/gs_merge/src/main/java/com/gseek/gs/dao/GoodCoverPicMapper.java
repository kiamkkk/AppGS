package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作good_cover表.
 *
 * @author Phak
 * @since 2023/5/9-13:00
 */
@Mapper
public interface GoodCoverPicMapper {
    /**
     * 记录商品封面图片储存路径
     * 应该在插入新商品时使用.
     * */
    int insertCoverPic(@Param("goodId") int goodId,@Param("list") List<String> paths);

    /**
     * 通过商品id,获取商品封面图片储存路径.
     * */
    List<String> selectCoversByGoodId(@Param("goodId") int goodId);

}
