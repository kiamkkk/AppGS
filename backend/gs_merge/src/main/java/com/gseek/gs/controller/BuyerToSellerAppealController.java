package com.gseek.gs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.business.GoodBO;
import com.gseek.gs.pojo.data.BillDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.*;
import com.gseek.gs.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



/**
 * @author: Isabella
 * @create: 2023-05-12 23:52
 **/
@RestController
@RequestMapping("/after_sale/buyer")
@Slf4j
public class BuyerToSellerAppealController {
    //TODO 图片名字也得放进来啊
    @Autowired
    BuyerToSellerAppealService buyerToSellerAppealService;
    @Autowired
    BillMapper billMapper;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    Result result;
    @PostMapping("/complain")
    public String addBuyerToSellerAppeal(@RequestParam("provePic") MultipartFile provePic, HttpServletRequest request,
                                         @RequestParam int billId,@RequestParam String appealReason,@RequestParam int myId,
                                         @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
//TODO 这里缺一个拿钱的
            int respondentId=billMapper.selectBillByBillId(billId).getSellerId();
            BigDecimal money=goodMapper.selectPriceByBillId(billId);
            if(money.compareTo(moneyMapper.selectMoneyBOByUserId(details.getUserId()).getRemain())<=-1){
                moneyMapper.returnMoney(billId);
            }
            else {
                //todo 缺一个退余额同时加入欠款的
            }
            String realPath= FileUtils.fileUtil(provePic,request);
            BuyerToSellerAppealDTO buyerToSellerAppealDTO=new BuyerToSellerAppealDTO(appealReason,realPath,billId,myId);
            buyerToSellerAppealService.addBuyerToSellerAppeal(buyerToSellerAppealDTO);
            return "ok";

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @GetMapping("/complain/{appealId}")
    public String queryAppeal(@PathVariable int appealId,
                                             @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBill_id();
//            int respondentId=billService.selectBillByBillId(billId).getSellerId();
//            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()
//            || respondentId!=details.getUserId()){
//                throw new ForbiddenException();
//            }
            return buyerToSellerAppealService.queryAppeal(appealId).toString();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    @DeleteMapping("/complain/{appealId}")
    public String deleteAppeal(@PathVariable int appealId,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            buyerToSellerAppealService.deleteAppeal(appealId);
            if(buyerToSellerAppealService.queryResult(appealId).isAppeal_result()){
                //TODO 解除钱包冻结和余额返还
                //todo 这里可能要查询一下在黑名单的id
                //从黑名单内删除
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
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBill_id();
//            int respondentId=billService.selectBillByBillId(billId).getSellerId();
//            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()
//                    || respondentId!=details.getUserId()){
//                throw new ForbiddenException();
//            }
            return buyerToSellerAppealService.queryResult(appealId).toString();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    //TODO 方法不确定能不能行，要试试
    @PatchMapping("/complain/{appealId}")
    public String updateAppeal(@PathVariable int appealId,@RequestParam int billId,HttpServletRequest request,
                               @RequestParam String appealReason,@RequestParam("provePic") MultipartFile provePic,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            String realPath=FileUtils.fileUtil(provePic,request);
            int myId=details.getUserId();
            BuyerToSellerAppealDTO buyerToSellerAppealDTO=new BuyerToSellerAppealDTO(appealReason,realPath,billId,myId);

            if(buyerToSellerAppealService.queryResult(appealId).isAppeal_result()){
                throw new ServerException("已被审核，无法更改");
            }
            buyerToSellerAppealService.updateAppeal(buyerToSellerAppealDTO);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }


    }
}
