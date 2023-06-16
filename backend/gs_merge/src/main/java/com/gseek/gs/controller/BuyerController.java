package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.pojo.dto.DeleteFavDTO;
import com.gseek.gs.pojo.dto.PostFavDTO;
import com.gseek.gs.pojo.dto.PostOfferPriceDTO;
import com.gseek.gs.service.inter.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 * 处理买家操作.
 *
 * @author Phak
 * @since 2023/5/11-22:17
 */
@RestController
@Slf4j
@RequestMapping("/buyer")
public class BuyerController implements Controller{

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    @Qualifier("buyerServiceImpl")
    BuyerService buyerService;

    /**
     * 买家获取所有出价记录
     * */
    @GetMapping("/{user_id}/offer_price")
    public String getAllOfferPrice(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                   @PathVariable("user_id") int userId)
            throws JsonProcessingException,ForbiddenException  {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (userId!=details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.getAllOfferPrice(userId);
    }

    /**
     * 买家获取所有收藏商品
     * */
    @GetMapping("/{user_id}/fav")
    public String getAllFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            @PathVariable("user_id") int userId)
            throws JsonProcessingException,ForbiddenException  {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (userId!=details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.getAllFav(userId);
    }

    /**
     * 买家获取所有已购买商品
     * */
    @GetMapping("/{user_id}/bought")
    public String getBoughtGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                 @PathVariable("user_id") int userId)
            throws JsonProcessingException,ForbiddenException  {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (userId != details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.getBoughtGoods(userId);
    }

    /**
     * 买家出价
     * */
    @PostMapping("/offer_price")
    public String postOfferPrice(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                 @RequestBody PostOfferPriceDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();
        // 鉴权
        if (dto.getOfferUserId() != details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.postOfferPrice(dto);
    }

    /**
     * 买家收藏商品
     * */
    @PostMapping("/{user_id}/fav")
    public String postFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                          @PathVariable("user_id") int userId, @RequestBody PostFavDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();
        // 鉴权
        if (userId!=details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.postFav(userId,dto);
    }

    /**
     * 买家取消收藏商品
     * */
    @DeleteMapping("/{user_id}/fav")
    public String deleteFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            @PathVariable("user_id") int userId, @RequestBody DeleteFavDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();
        // 鉴权
        if (userId!=details.getUserId()){
            throw ForbiddenException.gainNotAccess();
        }

        return buyerService.deleteFav(userId,dto);
    }
}
