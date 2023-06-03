package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;

/**
 * @author Phak
 * @since 2023/5/9-22:16
 */
public interface SellerService {
//todo 补充注释

    /**
     *
     * */
    String postGood(int userId, String userName, PostGoodsDTO dto) throws JsonProcessingException;

    /**
     *
     * */
    String patchGood(int userId, String userName, PatchGoodsDTO dto)
            throws JsonProcessingException, ParameterWrongException;

    /**
     *
     * */
    String deleteGood(int userId, int goodId) throws JsonProcessingException;

    /**
     *
     *
     */
    String getAllGoods(int userId) throws JsonProcessingException;


    /**
     *
     * */
    String getGoodsSold(int userId) throws JsonProcessingException;

    /**
     *
     * */
    String getSingleGoodOfferPrice(int goodId) throws JsonProcessingException;
}
