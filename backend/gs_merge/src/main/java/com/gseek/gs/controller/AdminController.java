package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.BillDO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.*;
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
 *
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
    Result result;
    //TODO 过滤器
    @GetMapping("/audit/report/unChecked")
    public List<BlacklistDO> queryAllUnchecked(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return blacklistService.queryAllUnchecked();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @PatchMapping("/audit/report/{blackId}")
    public String auditReport(@PathVariable int blackId, @RequestBody BlacklistResultBO blacklistResultBO,
                              @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {

        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            blacklistService.auditReport(blacklistResultBO);
            blacklistService.updateCheck(blackId);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @PutMapping("/audit/product/{goodId}")
    public String auditGood(@PathVariable int goodId, @RequestBody GoodCheckedDO goodCheckedDO,
                            @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            goodCheckedDO.setGoodId(goodId);
            adminService.auditGood(goodCheckedDO);
            return result.gainPutSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @PatchMapping("audit/after_sale/buyer_complain/{appealId}")
    public String auditBuyerAppeal(@PathVariable int appealId, @RequestBody BuyerToSellerAppealResultBO buyerToSellerAppealResultBO,
                                   @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            buyerToSellerAppealResultBO.setAppeal_id(appealId);
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBill_id();
            if (buyerToSellerAppealResultBO.isAppeal_result()){
//            成功扣钱
                moneyService.returnMoney(billId);
                int respondentId=billService.selectBill(billId).getSellerId();
                int goodId=billService.selectBill(billId).getGoodId();
                BigDecimal price=goodMapper.selectPriceByBillId(billId);
                if(moneyMapper.selectMoneyBOByUserId(respondentId).getRemain().compareTo(price)>0){
                    moneyMapper.unfrozenUser(respondentId);
                }
//            加入黑名单

                BuyerToSellerAppealBO b=buyerToSellerAppealService.queryAppeal(appealId);
                int claimerId =b.getMyId();

                String appealReason=buyerToSellerAppealService.queryAppeal(appealId).getAppeal_reason();
                String provePic=buyerToSellerAppealService.queryAppeal(appealId).getProvePic();
                BlacklistDTO blacklistDTO=new BlacklistDTO(claimerId,respondentId,appealReason,provePic);
                blacklistService.addReport(blacklistDTO);
                int blackId=blacklistService.queryBlackId(claimerId,respondentId);
                BlacklistResultBO blacklistResultBO=new BlacklistResultBO(true,true,adminDetails.getAdminId(),blackId,"");
                blacklistService.auditReport(blacklistResultBO);
            }
            adminService.auditBuyerAppeal(buyerToSellerAppealResultBO);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @PatchMapping("/audit/after_sale/seller_complain/{appealId}")
    public String auditSellerAppeal(@PathVariable int appealId, @RequestBody SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO,
                                    @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            sellerToBuyerAppealResultBO.setAdmin_id(appealId);
            int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
            if (sellerToBuyerAppealResultBO.isAppeal_result()){
                if(sellerToBuyerAppealResultBO.isAccept()){
                    //TODO 按损伤程度赔偿要写,程度为3，1的不用
//            moneyService


                }
                if(sellerToBuyerAppealResultBO.getDamage_degree()==3){
                    //TODO 通知买家
//                返回钱
                    moneyService.returnMoney(billId);
                    //加入黑名单
                    SellerToBuyerAppealBO b=sellerToBuyerAppealService.queryAppeal(appealId);
                    int claimerId =b.getMyId();
                    int respondentId=billService.selectBill(billId).getBuyerId();
                    String appealReason=sellerToBuyerAppealService.queryAppeal(appealId).getAppeal_reason();
                    String provePic=sellerToBuyerAppealService.queryAppeal(appealId).getPic_after();
                    BlacklistDTO blacklistDTO=new BlacklistDTO(claimerId,respondentId,appealReason,provePic);
                    blacklistService.addReport(blacklistDTO);
                    int blackId=blacklistService.queryBlackId(claimerId,respondentId);
                    BlacklistResultBO blacklistResultBO=new BlacklistResultBO(true,true,adminDetails.getAdminId(),blackId,"");
                    blacklistService.auditReport(blacklistResultBO);
                }
                if(sellerToBuyerAppealResultBO.getDamage_degree()==1){
                    //TODO 告知卖家结束过程，协调程序不确定怎么跳转
                }

            }
            //TODO 还没写调解程序
            adminService.auditSellerAppeal(sellerToBuyerAppealResultBO);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @GetMapping("/audit/product/all")
    public List<GoodBO> queryUnCheckedProduct(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedProduct();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @GetMapping("/after_sale/seller_complain/all")
    public List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedSellerAppeal();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @GetMapping("/after_sale/buyer_complain/all")
    public List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal(@CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return adminService.queryUnCheckedBuyerAppeal();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
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

}
