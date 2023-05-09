package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface GoodDetailPicMapper {
    //todo 补充注释

    List<String> selectDetailsByGoodId(@Param("goodId") int goodId);

    int insertDetailPic(@Param("goodId") int goodId,@Param("list") List<String> paths);
}
