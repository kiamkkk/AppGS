package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.BillDO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.*;
import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.NoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 对应 /admin/** 下的操作
 *管理员功能
 * @author Isabella
 * @since 2023/5/7-21:30
 */
@RestController
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    BuyerToSellerAppealService buyerToSellerAppealService;
    @Autowired
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @Autowired
    MoneyService moneyService;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    BillService billService;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MessageController messageController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Result result;
    /**
    *查看未审核的黑名单
    * */
    @GetMapping("/audit/report/unChecked")
    public List<BlacklistDO> queryAllUnchecked(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return blacklistService.queryAllUnchecked();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为adminWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     *审核的黑名单
     * */
    @PatchMapping("/audit/report/{blackId}")
    public String auditReport(@PathVariable int blackId, @RequestBody BlacklistResultBO blacklistResultBO,
                              @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {

        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            blacklistService.auditReport(blacklistResultBO,blackId);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为adminWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     *审核商品
     * */
    @PutMapping("/audit/product/{goodId}")
    public String auditGood(@PathVariable int goodId, @RequestBody GoodCheckedDO goodCheckedDO,
                            @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            goodCheckedDO.setGoodId(goodId);
//            审核商品
            adminService.auditGood(goodCheckedDO);
            return result.gainPutSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为adminWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     *审核卖家举报买家
     * */
    @PatchMapping("audit/after_sale/buyer_complain/{appealId}")
    public String auditBuyerAppeal(@PathVariable int appealId, @RequestBody BuyerToSellerAppealResultBO buyerToSellerAppealResultBO,
                                   @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            int adminId=adminDetails.getAdminId();
           adminService.auditBuyerAppeal(buyerToSellerAppealResultBO,appealId,adminId);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为adminWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     *审核卖家举报买家
     * */
    @PatchMapping("/audit/after_sale/seller_complain/{appealId}")
    public String auditSellerAppeal(@PathVariable int appealId, @RequestBody SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO,
                                    @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            int adminId=adminDetails.getAdminId();
//          审核申诉
            adminService.auditSellerAppeal(sellerToBuyerAppealResultBO,appealId,adminId);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
    * 查看所有未审核商品
    * */
    @GetMapping("/audit/product/all")
    public List<GoodBO> queryUnCheckedProduct(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedProduct();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
    * 查看所有没有审核的卖家申诉
    * */
    @GetMapping("/after_sale/seller_complain/all")
    public List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedSellerAppeal();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看所有没有审核的买家申诉
     * */
    @GetMapping("/after_sale/buyer_complain/all")
    public List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedBuyerAppeal();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看卖家申诉
     * */
    @GetMapping("/after_sale/seller_complain/{appealId}")
    public SellerToBuyerAppealBO querySellerAppealById(@PathVariable int appealId,
                                                       @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.querySellerAppealById(appealId);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看买家申诉
     * */
    @GetMapping("/after_sale/buyer_complain/{appealId}")
    public BuyerToSellerAppealBO queryBuyerAppealById(@PathVariable int appealId,
                                                      @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryBuyerAppealById(appealId);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看订单内商品的账号和密码
    * */
    @GetMapping("/admin/queryAccount/{billId}")
    public GoodAccountBO queryAccount(@PathVariable int billId,
                                      @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return goodMapper.selectGoodAccountByBillId(billId);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

}
