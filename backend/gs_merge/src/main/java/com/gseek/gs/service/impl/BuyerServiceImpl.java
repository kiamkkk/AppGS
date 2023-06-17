package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.BoughtGoodMapper;
import com.gseek.gs.dao.GoodFavMapper;
import com.gseek.gs.dao.OfferPriceMapper;
import com.gseek.gs.pojo.business.BoughtGoodBO;
import com.gseek.gs.pojo.business.GoodFavBO;
import com.gseek.gs.pojo.business.GoodOfferPriceBO;
import com.gseek.gs.pojo.dto.DeleteFavDTO;
import com.gseek.gs.pojo.dto.PostFavDTO;
import com.gseek.gs.pojo.dto.PostOfferPriceDTO;
import com.gseek.gs.service.inter.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Phak
 * @since 2023/5/11-22:22
 */
@Service("buyerServiceImpl")
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Result result;
    @Autowired
    OfferPriceMapper offerPriceMapper;
    @Autowired
    GoodFavMapper goodFavMapper;
    @Autowired
    BoughtGoodMapper boughtGoodMapper;


    @Override
    public String postOfferPrice(PostOfferPriceDTO dto) throws JsonProcessingException {
        offerPriceMapper.insertOfferPrice(dto);
        return result.gainPostSuccess();
    }

    @Override
    public String getAllOfferPrice(int userId) throws JsonProcessingException {
        List<GoodOfferPriceBO> bos=offerPriceMapper.selectAllOfferPrice(userId);
        return objectMapper.writeValueAsString(bos);
    }

    @Override
    public String getAllFav(int userId) throws JsonProcessingException {
        List<GoodFavBO> bos=goodFavMapper.selectAllFavByUserId(userId);
        return objectMapper.writeValueAsString(bos);
    }

    @Override
    public String getBoughtGoods(int userId) throws JsonProcessingException {
        List<BoughtGoodBO> bos=boughtGoodMapper.selectBoughtGoodsByUserId(userId);
        return objectMapper.writeValueAsString(bos);
    }

    @Override
    public String postFav(int userId, PostFavDTO dto) throws JsonProcessingException {
        goodFavMapper.insertFav(userId,dto.getGoodId(),dto.getTime());
        return result.gainPostSuccess();
    }

    @Override
    public String deleteFav(int userId, DeleteFavDTO dto) throws JsonProcessingException {
        goodFavMapper.deleteFav(userId,dto.getGoodId());
        return result.gainDeleteSuccess();
    }
}
