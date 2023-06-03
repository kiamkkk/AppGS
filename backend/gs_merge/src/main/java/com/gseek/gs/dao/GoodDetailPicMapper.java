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
     * 记录商品详情图片储存路径.
     * */
    int insertDetailPic(@Param("goodId") int goodId,@Param("list") List<String> paths);
    /**
     * 删除goodId对应的所有封面路径.
     * 物理删除.
     * */
    int deleteAllDetailPicByGoodId(@Param("goodId") int goodId);
    /**
     * 更新goodId对应的商品封面路径.
     * 先调用deleteAllDetailPicByGoodId删除所有的旧记录,
     * 再调用insertDetailPic插入新的记录.
     * */
    // todo 事实上,这里的sql是直接将insertDetailPic和deleteAllDetailPicByGoodId的sql复制黏贴拼起来的;我希望能重复使用这两句sql,但找不到办法
    int updateDetailPictureUrl(@Param("goodId") int goodId, @Param("list") List<String> paths);
    /**
     * 记录商品详情图片储存路径
     * 应该在插入新商品时使用.
     * */
    List<String> selectDetailsByGoodId(@Param("goodId") int goodId);
}
