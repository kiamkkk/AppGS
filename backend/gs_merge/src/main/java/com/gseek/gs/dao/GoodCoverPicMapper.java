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
     * */
    int insertCoverPic(@Param("goodId") int goodId, @Param("list") List<String> paths);
    /**
     * 删除goodId对应的所有封面路径.
     * 物理删除.
     * */
    int deleteAllCoverPicByGoodId(@Param("goodId") int goodId);
    /**
     * 更新goodId对应的商品封面路径.
     * 先调用deleteAllCoverPicByGoodId删除所有的旧记录,
     * 再调用insertCoverPic插入新的记录.
     * */
    // todo 事实上,这里的sql是直接将insertCoverPic和deleteAllCoverPicByGoodId的sql复制黏贴拼起来的;我希望能重复使用这两句sql,但找不到办法
    int updateCoverPictureUrl(@Param("goodId") int goodId, @Param("list") List<String> paths);
    /**
     * 通过商品id,获取商品封面图片储存路径.
     * */
    List<String> selectCoversByGoodId(@Param("goodId") int goodId);

}
