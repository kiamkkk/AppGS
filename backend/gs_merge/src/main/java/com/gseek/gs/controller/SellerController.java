package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;
import com.gseek.gs.service.inter.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 * 处理卖家操作.
 *
 * @author Phak
 * @since 2023/5/9-22:14
 */
@RestController
@RequestMapping("/seller")
@Slf4j
public class SellerController implements Controller {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("sellerServiceImpl")
    SellerService sellerService;

    /**
     * 卖家获取名下所有商品
     * */
    @GetMapping("/{user_id}/goods")
    public String getAllGoods(
            @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
            @PathVariable("user_id") int userId
        ) throws ForbiddenException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (userId != details.getUserId() ){
            throw new ForbiddenException();
        }

        return sellerService.getAllGoods(details.getUserId());
    }

    /**
     * 卖家获取所有已卖出商品
     * */
    @GetMapping("/{user_id}/goods/sold")
    public String getGoodsSold(
            @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
            @PathVariable("user_id") int userId
    ) throws ForbiddenException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (userId != details.getUserId() ){
            throw new ForbiddenException();
        }

        return sellerService.getGoodsSold(details.getUserId());
    }

    /**
     * 卖家获取某商品出价信息
     * */
    @GetMapping("/{good_id}/offer_price")
    public String getSingleGoodOfferPrice(@PathVariable("good_id") int goodId)
            throws JsonProcessingException {
        return sellerService.getSingleGoodOfferPrice(goodId);
    }
    /**
     * 卖家发布商品
     * Content-Type:multipart/form-data
     * */
    @PostMapping("/goods")
    public String postGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                            HttpServletRequest request)
            throws JsonProcessingException, ServerException {

        CustomWebAuthenticationDetails details =perService(authentication);
        PostGoodsDTO dto=new PostGoodsDTO(request);
        dto.perService();

        return sellerService.postGood(details.getUserId(),dto.getOwnUserName(),dto);
    }

    /**
     * 卖家修改商品.
     * 商品正在交易时禁止修改.
     * Content-Type:multipart/form-data
     * */
    @PatchMapping("/goods")
    public String patchGoods(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             HttpServletRequest request)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);

        PatchGoodsDTO dto =new PatchGoodsDTO(request);
        dto.perService();

        return sellerService.patchGood(details.getUserId(),dto.getOwnUserName(),dto);
    }
    /**
     * 卖家删除某一商品
     * 商品正在交易时禁止删除.
     * */
    @DeleteMapping("/goods/{good_id}")
    public String deleteGood(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             @PathVariable("good_id") int goodId)
            throws ForbiddenException, JsonProcessingException {
        CustomWebAuthenticationDetails details = perService(authentication);

        return sellerService.deleteGood(details.getUserId(), goodId);
    }
}
