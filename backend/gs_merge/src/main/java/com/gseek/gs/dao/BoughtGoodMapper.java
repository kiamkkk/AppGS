package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BoughtGoodBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/12-11:14
 */
@Mapper
public interface BoughtGoodMapper {

    /**
     *
     * */
    List<BoughtGoodBO> selectBoughtGoodsByUserId(@Param("userId") int userId);
}
