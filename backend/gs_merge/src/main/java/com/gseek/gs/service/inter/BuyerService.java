package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.dto.DeleteFavDTO;
import com.gseek.gs.pojo.dto.PostFavDTO;
import com.gseek.gs.pojo.dto.PostOfferPriceDTO;

/**
 * @author Phak
 * @since 2023/5/11-22:22
 */
public interface BuyerService {
    //todo 补充注释
    /**
     *
     * */
    String postOfferPrice(PostOfferPriceDTO dto) throws JsonProcessingException;

    /**
     *
     * */
    String getAllOfferPrice(int userId) throws JsonProcessingException;

    /**
     *
     * */
    String getAllFav(int userId) throws JsonProcessingException;

    String postFav(int userId, PostFavDTO dto) throws JsonProcessingException;

    String deleteFav(int userId, DeleteFavDTO dto) throws JsonProcessingException;

}
