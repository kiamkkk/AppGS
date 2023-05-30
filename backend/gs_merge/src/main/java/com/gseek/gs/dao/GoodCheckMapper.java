package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作good_checked表.
 *
 * @author Phak
 * @since 2023/5/21-18:06
 */
@Mapper
public interface GoodCheckMapper {

    /**
     * 插入待审核商品.
     * 卖家发布新商品后,先进行审核,审核通过后才能被搜索到.
     * */
    int insertNewGood(@Param("goodId") int goodId);

}
