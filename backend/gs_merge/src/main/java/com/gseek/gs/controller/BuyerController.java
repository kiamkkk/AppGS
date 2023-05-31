package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 处理买家操作.
 *
 * @author Phak
 * @since 2023/5/11-22:17
 */
@RestController
@Slf4j
@RequestMapping("/buyer")
public class BuyerController {

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
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (userId!=details.getUserId()){
                throw new ForbiddenException();
            }

            return buyerService.getAllOfferPrice(userId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    /**
     * 买家获取所有收藏商品
     * */
    @GetMapping("/{user_id}/fav")
    public String getAllFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            @PathVariable("user_id") int userId)
            throws JsonProcessingException,ForbiddenException  {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (userId!=details.getUserId()){
                throw new ForbiddenException();
            }

            return buyerService.getAllFav(userId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    /**
     * 买家获取所有已购买商品
     * */
    @GetMapping("/{user_id}/bought")
    public String getBoughtGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                 @PathVariable("user_id") int userId)
            throws JsonProcessingException,ForbiddenException  {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (userId != details.getUserId()){
                throw new ForbiddenException();
            }

            return buyerService.getBoughtGoods(userId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    /**
     * 买家出价
     * */
    @PostMapping("/offer_price")
    public String postOfferPrice(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                 @RequestBody PostOfferPriceDTO dto)
            throws JsonProcessingException, ForbiddenException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (dto.getOfferUserId() != details.getUserId()){
                throw new ForbiddenException();
            }
            return buyerService.postOfferPrice(dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    /**
     * 买家收藏商品
     * */
    @PostMapping("/{user_id}/fav")
    public String postFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                          @PathVariable("user_id") int userId, PostFavDTO dto)
            throws JsonProcessingException, ForbiddenException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (userId!=details.getUserId()){
                throw new ForbiddenException();
            }

            return buyerService.postFav(userId,dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    /**
     * 买家取消收藏商品
     * */
    @DeleteMapping("/{user_id}/fav")
    public String deleteFav(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            @PathVariable("user_id") int userId, DeleteFavDTO dto)
            throws JsonProcessingException, ForbiddenException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (userId!=details.getUserId()){
                throw new ForbiddenException();
            }

            return buyerService.deleteFav(userId,dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }
}
