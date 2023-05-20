package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
import com.gseek.gs.service.inter.AdminService;
import com.gseek.gs.service.inter.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    Result result;
    //TODO 过滤器
    @GetMapping("/audit/report/unChecked")
    public List<BlacklistDO> queryAllUnchecked(){
        return blacklistService.queryAllUnchecked();
    }
//TODO 外面的值传不进来（？
    @PatchMapping("/audit/report/{blackId}")
    public String auditReport(@PathVariable int blackId, @RequestBody AdminBlacklistDTO adminBlacklistDTO) throws JsonProcessingException {
        blacklistService.auditReport(adminBlacklistDTO);
        blacklistService.updateCheck(blackId);
        return result.gainPatchSuccess();
    }
    @PutMapping("/audit/product/{goodId}")
    public String auditGood(@PathVariable int goodId, @RequestBody GoodCheckedDO goodCheckedDO) throws JsonProcessingException {
        goodCheckedDO.setGoodId(goodId);
        adminService.auditGood(goodCheckedDO);
        return result.gainPutSuccess();
    }
    @PatchMapping("audit/after_sale/buyer_complain/{appealId}")
    public String auditBuyerAppeal(@PathVariable int appealId, @RequestBody BuyerToSellerAppealResultBO buyerToSellerAppealResultBO){
        buyerToSellerAppealResultBO.setAppeal_id(appealId);
        if (buyerToSellerAppealResultBO.isAppeal_result()){

        }
        adminService.auditBuyerAppeal(buyerToSellerAppealResultBO);
        return "ok";
    }
    @PatchMapping("/audit/after_sale/seller_complain/{appealId}")
    public String auditSellerAppeal(@PathVariable int appealId, @RequestBody SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO){
        sellerToBuyerAppealResultBO.setAdmin_id(appealId);
        adminService.auditSellerAppeal(sellerToBuyerAppealResultBO);
        return "ok";
    }
    @GetMapping("/audit/product/all")
    public List<GoodBO> queryUnCheckedProduct(){
        return adminService.queryUnCheckedProduct();
    }
    @GetMapping("/after_sale/seller_complain/all")
    public List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal(){
        return adminService.queryUnCheckedSellerAppeal();
    }
    @GetMapping("/after_sale/buyer_complain/all")
    public List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal(){
        return adminService.queryUnCheckedBuyerAppeal();
    }
    @GetMapping("/after_sale/seller_complain/{appealId}")
    public SellerToBuyerAppealBO querySellerAppealById(@PathVariable int appealId){
        return adminService.querySellerAppealById(appealId);
    }
    @GetMapping("/after_sale/buyer_complain/{appealId}")
    public BuyerToSellerAppealBO queryBuyerAppealById(@PathVariable int appealId){
        return adminService.queryBuyerAppealById(appealId);
    }

}
