package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.business.GoodBO;
import com.gseek.gs.pojo.business.GoodsWithoutAccountAndSoldBO;
import com.gseek.gs.pojo.data.GoodDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-12:38
 */
@Mapper
public interface GoodMapper {

    //todo 补充注释

    /**
     * 查出所有信息
     *
     *
     *
     * */
    GoodDO selectGoodByGoodIdFully(int goodId);

    /**
     *
     * */
    GoodsWithoutAccountAndSoldBO selectGoodByGoodIdWithoutAccount(@Param("goodId") int goodId);

    /**
     *
     * */
    int insertGood(@Param("goodDO") GoodDO goodDO);

    /**
     *
     * */
    int updateGoodSelect(@Param("good") GoodDO goodDO);

    /**
     * 删除good、good_tag、good_Fav、good_cover_pic、good_detail_pic、bill下的信息
     *
     * */
    @Transactional
    int deleteGood(@Param("goodId") int goodId);

    /**
     *
     * */
    Integer selectOwnUserIdByGoodId(@Param("goodId") int goodId);

    /**
     *
     * */
    //todo list都是null
    List<GoodBO> selectGoodsByUserIdWithoutTypeTagId(@Param("userId") int userId);
    //todo list都是null
    /**
     *
     * */
    List<GoodBO> selectGoodsSoldByUserIdWithoutTypeTagId(@Param("userId") int userId);

    /**
     *
     * */
    BigDecimal selectPriceByBillId(@Param("billId") int billId);

    GoodAccountBO selectGoodAccountByBillId(@Param("billId") int billId);

    /**
     * 将原ownUserName为的originUserName的商品修改为patchUserName
     * */
    int updateOwnUserName(@Param("patchUserName") String patchUserName,@Param("originUserName") String originUserName);

}
