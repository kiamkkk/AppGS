package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.GoodOfferPriceBO;
import com.gseek.gs.pojo.business.OfferPriceBO;
import com.gseek.gs.pojo.dto.PostOfferPriceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作offer_price表.
 *
 * @author Phak
 * @since 2023/5/11-16:49
 */
@Mapper
public interface OfferPriceMapper {

    /**
     * 新增出价信息.
     * */
    // todo 这里是不是用offerPriceDO更好?
    int insertOfferPrice(@Param("dto") PostOfferPriceDTO dto);

    /**
     * 根据商品id,获取所有出价信息.
     * */
    List<OfferPriceBO> selectOfferPriceByGoodId(@Param("goodId") int goodId);

    /**
     * 根据用户id,获取买家的所有出价记录.
     * */
    List<GoodOfferPriceBO> selectAllOfferPrice(@Param("userId") int userId);

}
