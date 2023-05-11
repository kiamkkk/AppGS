package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodFavBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-13:00
 */
@Mapper
public interface GoodFavMapper {
    //todo 补充注释
    /**
     *
     */
    List<GoodFavBO> getAllFav(@Param("userId") int userId);

    /**
     *
     * */
    int insertFav(@Param("userId") int userId,@Param("goodId") int goodId,@Param("time") long time);

    /**
     *
     * */
    int deleteFav(@Param("userId") int userId,@Param("goodId") int goodId);
}
