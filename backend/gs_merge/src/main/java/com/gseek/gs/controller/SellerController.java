package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;
import com.gseek.gs.service.inter.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/9-22:14
 */
@RestController
@RequestMapping("/seller")
@Slf4j
public class SellerController {
    //todo 补充注释
    //todo 只能持有人访问这些接口


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("sellerServiceImpl")
    SellerService sellerService;

    @PostMapping("/goods")
    public String postGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            PostGoodsDTO dto) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            
            return sellerService.postGood(details.getUserId(),dto.getOwnUserName(),dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @PatchMapping("/goods")
    public String patchGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             PatchGoodsDTO dto) throws JsonProcessingException, ForbiddenException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return sellerService.patchGood(details.getUserId(),dto.getOwnUserName(),dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @DeleteMapping("/goods/{good_id}")
    public String deleteGood(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             @PathVariable("good_id") int goodId)
            throws ForbiddenException, JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return sellerService.deleteGood(details.getUserId(), goodId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @GetMapping("/{username}/goods")
    public String getAllGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws ForbiddenException, JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return sellerService.getAllGoods(details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @GetMapping("/{username}/goods/sold")
    public String getGoodsSold(@CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws ForbiddenException, JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return sellerService.getGoodsSold(details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @GetMapping("/{good_id}/offer_price")
    public String getSingleGoodOfferPrice(@PathVariable("good_id") int goodId)
            throws JsonProcessingException {
        return sellerService.getSingleGoodOfferPrice(goodId);
    }

}
