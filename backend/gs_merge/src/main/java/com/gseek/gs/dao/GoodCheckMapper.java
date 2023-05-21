package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/21-18:06
 */
@Mapper
public interface GoodCheckMapper {

    /**
     *
     * */
    int insertNewGood(@Param("goodId") int goodId);

}
