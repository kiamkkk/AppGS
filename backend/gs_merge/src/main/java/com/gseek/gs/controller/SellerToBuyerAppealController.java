package com.gseek.gs.controller;


import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;
import com.gseek.gs.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;



/**
 * @author: Isabella
 * @create: 2023-05-13 23:35
 **/
@RestController
@RequestMapping("/after_sale/seller")
@Slf4j
public class SellerToBuyerAppealController {
    //TODO 和buyer的controller一样的TODO也要改
    //TODO 图片名字也得放进来啊
    @Autowired
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @Autowired
    BillService billService;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    BillMapper billMapper;
    @Autowired
    Result result;
    @PostMapping("/complain")
    public String addSellerToBuyerAppeal(@RequestParam("pic_before") MultipartFile picBefore,@RequestParam("pic_after") MultipartFile picAfter, HttpServletRequest request,
                                         @RequestParam int billId, @RequestParam String appealReason, @RequestParam int myId,@RequestParam boolean accept) throws JsonProcessingException {
        String pathBefore= FileUtils.fileUtil(picBefore,request);
        String pathAfter=FileUtils.fileUtil(picAfter,request);
        SellerToBuyerAppealDTO sellerToBuyerAppealDTO=new SellerToBuyerAppealDTO(appealReason,pathBefore,pathAfter,accept,billId,myId);
        sellerToBuyerAppealService.addSellerToBuyerAppeal(sellerToBuyerAppealDTO);
        return result.gainPostSuccess();
    }
    @GetMapping("/complain/{appealId}")
    public SellerToBuyerAppealBO queryAppeal(@PathVariable int appealId,
                                             @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return sellerToBuyerAppealService.queryAppeal(appealId);
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            return sellerToBuyerAppealService.queryAppeal(appealId);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @DeleteMapping("/complain/{appealId}")
    public String deleteAppeal(@PathVariable int appealId,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            sellerToBuyerAppealService.deleteAppeal(appealId);
            if(sellerToBuyerAppealService.queryResult(appealId).isAppeal_result()){
                //TODO 余额返还
                int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
                moneyMapper.unfrozenUser(billMapper.selectBill(billId).getSellerId());

                //从黑名单内删除
                //todo 这里可能要查询一下在黑名单的id
                blacklistService.deleteReport(appealId);
            }
            return result.gainDeleteSuccess();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            sellerToBuyerAppealService.deleteAppeal(appealId);
            if(sellerToBuyerAppealService.queryResult(appealId).isAppeal_result()){
                //TODO 余额返还
                int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
                moneyMapper.unfrozenUser(billMapper.selectBill(billId).getSellerId());

                //从黑名单内删除
                //todo 这里可能要查询一下在黑名单的id
                blacklistService.deleteReport(appealId);
            }
            return result.gainDeleteSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @GetMapping("/query/audit/{appealId}")
    public String queryResult(@PathVariable int appealId,
                              @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return sellerToBuyerAppealService.queryResult(appealId).toString();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
            int respondentId=sellerToBuyerAppealService.selectBillByBillId(billId).getBuyerId();
            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()
                    || respondentId!=details.getUserId()){
                throw new ForbiddenException();
            }
            return sellerToBuyerAppealService.queryResult(appealId).toString();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @PatchMapping("/complain/{appealId}")
    public String updateAppeal(@PathVariable int appealId,@RequestParam("pic_before") MultipartFile picBefore,@RequestParam("pic_after") MultipartFile picAfter, HttpServletRequest request,
                               @RequestParam int billId, @RequestParam String appealReason,@RequestParam boolean accept,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            String pathBefore= FileUtils.fileUtil(picBefore,request);
            String pathAfter=FileUtils.fileUtil(picAfter,request);
            int myId=sellerToBuyerAppealService.queryMyId(appealId);
            SellerToBuyerAppealDTO sellerToBuyerAppealDTO=new SellerToBuyerAppealDTO(appealReason,pathBefore,pathAfter,accept,billId,myId);
            if(sellerToBuyerAppealService.queryResult(appealId).isAppeal_result()){
                throw new ServerException("已被审核，无法更改");
            }
            sellerToBuyerAppealService.updateAppeal(sellerToBuyerAppealDTO);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
}
