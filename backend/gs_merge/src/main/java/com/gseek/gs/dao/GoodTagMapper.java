package com.gseek.gs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodTagMapper {

    List<Integer> selectGoodIdByTagId(@Param("tagId") int tagId);


}
