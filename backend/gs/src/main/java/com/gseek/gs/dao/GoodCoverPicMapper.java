package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-13:00
 */
@Mapper
public interface GoodCoverPicMapper {
    //todo 补充注释
    /**
     *
     * */
    List<String> selectCoversByGoodId(@Param("goodId") int goodId);

    /**
     *
     * */
    int insertCoverPic(@Param("goodId") int goodId,@Param("list") List<String> paths);

}
