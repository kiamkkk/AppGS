package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodOfferPriceBO;
import com.gseek.gs.pojo.business.OfferPriceBO;
import com.gseek.gs.pojo.dto.PostOfferPriceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Phak
 * @since 2023/5/11-16:49
 */
@Mapper
public interface OfferPriceMapper {
//todo 补充注释
    /**
     *
     * */
    List<OfferPriceBO> selectOfferPriceByGoodId(@Param("goodId") int goodId);

    int insertOfferPrice(@Param("dto")PostOfferPriceDTO dto);

    List<GoodOfferPriceBO> getAllOfferPrice(@Param("userId") int userId);

}
