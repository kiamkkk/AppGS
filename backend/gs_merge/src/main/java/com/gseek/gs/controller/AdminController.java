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
            blacklistService.auditReport(blacklistResultBO);
            blacklistService.updateCheck(blackId);
            if(blacklistResultBO.isAppeal_result()){
//                如果被加入黑名单提醒用户
                BlacklistBO blacklistBO=blacklistService.queryReport(blackId);
                messageController.blacklist(blacklistBO);
            }
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
            if(goodCheckedDO.isResult()){
//                通过审核通知卖家
                int toUserId=goodMapper.selectOwnUserIdByGoodId(goodId);
                NoticeMessage noticeMessage=new NoticeMessage("商品审核通过",System.currentTimeMillis(),toUserId);
                messageController.general(noticeMessage);
            }
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
            buyerToSellerAppealResultBO.setAppeal_id(appealId);
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBill_id();
            if (buyerToSellerAppealResultBO.isAppeal_result()){
//                成功扣钱
                int respondentId=billService.selectBill(billId).getSellerId();
                moneyService.returnMoney(billId,respondentId);
                int goodId=billService.selectBill(billId).getGoodId();
                BigDecimal price=goodMapper.selectPriceByBillId(billId);
//                如果余额足够就解冻
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
//               审核加入黑名单
                blacklistService.auditReport(blacklistResultBO);
                BlacklistBO blacklistBO=blacklistService.queryReport(blackId);
//                通知用户
                AppealMessageBean appealMessageBean=buyerToSellerAppealService.message(appealId);
                messageController.appeal(appealMessageBean);
                messageController.blacklist(blacklistBO);
//                审核卖家投诉
                adminService.auditBuyerAppeal(buyerToSellerAppealResultBO);
            }
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
            sellerToBuyerAppealResultBO.setAdmin_id(appealId);
            int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
//            审核通过&&卖家同意按协议走
            if (sellerToBuyerAppealResultBO.isAppeal_result()&&sellerToBuyerAppealResultBO.isAccept()){
                int claimerId =sellerToBuyerAppealService.queryAppeal(appealId).getMyId();
                int respondentId=billService.selectBill(billId).getBuyerId();
                if(sellerToBuyerAppealResultBO.isAccept()){
//                    账号价值受损严重
                    if(sellerToBuyerAppealResultBO.getDamage_degree()==3){
                        //加入黑名单

//                    返回钱
                        moneyService.returnMoney(billId,respondentId);
//                        加入黑名单
                        String appealReason=sellerToBuyerAppealService.queryAppeal(appealId).getAppeal_reason();
                        String provePic=sellerToBuyerAppealService.queryAppeal(appealId).getPic_after();
                        BlacklistDTO blacklistDTO=new BlacklistDTO(claimerId,respondentId,appealReason,provePic);
                        blacklistService.addReport(blacklistDTO);
                        int blackId=blacklistService.queryBlackId(claimerId,respondentId);
                        BlacklistResultBO blacklistResultBO=new BlacklistResultBO(true,true,adminDetails.getAdminId(),blackId,"");
//                       审核黑名单
                        blacklistService.auditReport(blacklistResultBO);
                        BlacklistBO blacklistBO=blacklistService.queryReport(blackId);
//                        消息通知
                        messageController.blacklist(blacklistBO);
                        AppealMessageBean appealMessageBean=buyerToSellerAppealService.message(appealId);
                        messageController.appeal(appealMessageBean);
                    }
                    else {
                        moneyService.returnMoneyByDegree(billId,sellerToBuyerAppealResultBO.getDamage_degree(),respondentId);
                    }
                }

                // 账号没有损伤
                if(!sellerToBuyerAppealResultBO.isAppeal_result()){
                    NoticeMessage noticeMessage=new NoticeMessage("您的账号未损伤，申诉不通过，有疑问可以询问客服",System.currentTimeMillis(),claimerId);
                    messageController.general(noticeMessage);
                }
//                卖家不同意按协议走&&审核通过
                if(sellerToBuyerAppealResultBO.isAppeal_result()&&!sellerToBuyerAppealResultBO.isAccept()){
                    NoticeMessage noticeMessage=new NoticeMessage("申诉通过，请与买家私下调解，有疑问可以询问客服",System.currentTimeMillis(),claimerId);
                    messageController.general(noticeMessage);
//                    通知买家
                    int goodId=billService.selectBill(billId).getGoodId();
                    String goodName=goodMapper.selectGoodByGoodIdFully(goodId).getGoodName();
                    String appealReason=sellerToBuyerAppealService.queryAppeal(appealId).getAppeal_reason();
                    String message="您因"+appealReason+"被商品"+goodName+"的卖家投诉成功，因对方不接收按协议退款，请私下和解，有疑问可以询问客服";
                    NoticeMessage noticeMessage1=new NoticeMessage(message,System.currentTimeMillis(),respondentId);
                    messageController.general(noticeMessage1);
                }
            }
//          审核申诉
            adminService.auditSellerAppeal(sellerToBuyerAppealResultBO);
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
    public GoodAccountBO queryAccount(@PathVariable int billId){
        GoodAccountBO goodAccountBO=goodMapper.selectGoodAccountByBillId(billId);
        return goodAccountBO;
    }

}
