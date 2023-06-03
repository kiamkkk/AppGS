package com.gseek.gs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;
import com.gseek.gs.util.FileUtils;
import com.gseek.gs.websocket.controller.MessageController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 卖家申诉买家
 * @author: Isabella
 * @create: 2023-05-13 23:35
 **/
@RestController
@RequestMapping("/after_sale/seller")
@Slf4j
public class SellerToBuyerAppealController {

    @Autowired
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @Autowired
    BillService billService;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    MoneyService moneyService;
    @Autowired
    BillMapper billMapper;
    @Autowired
    MessageController messageController;
    @Autowired
    Result result;
    /**
     * 新增申诉
     * */
    @PostMapping("/complain")
    public String addSellerToBuyerAppeal(@RequestParam("pic_before") MultipartFile picBefore,@RequestParam("pic_after") MultipartFile picAfter, HttpServletRequest request,
                                         @RequestParam int billId, @RequestParam String appealReason, @RequestParam int myId,@RequestParam boolean accept) throws JsonProcessingException {
        String pathBefore= FileUtils.fileUtil(picBefore,request);
        String pathAfter=FileUtils.fileUtil(picAfter,request);
        SellerToBuyerAppealDTO sellerToBuyerAppealDTO=new SellerToBuyerAppealDTO(appealReason,pathBefore,pathAfter,accept,billId,myId);
        sellerToBuyerAppealService.addSellerToBuyerAppeal(sellerToBuyerAppealDTO);
        return result.gainPostSuccess();
    }
    /**
     * 查看申诉
     * */
    @GetMapping("/complain/{appealId}")
    public SellerToBuyerAppealBO queryAppeal(@PathVariable int appealId,
                                             @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
//       管理员直接查看
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return sellerToBuyerAppealService.queryAppeal(appealId);
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
            int respondentId=billService.selectBill(billId).getBuyerId();
//            申诉者和被申诉者才能看
            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()
                    && respondentId!=details.getUserId()){
                throw new ForbiddenException();
            }
            return sellerToBuyerAppealService.queryAppeal(appealId);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 删除申诉
    * */
    @DeleteMapping("/complain/{appealId}")
    public String deleteAppeal(@PathVariable int appealId,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
//          如果申诉已经通过审核
            if(sellerToBuyerAppealService.queryResult(appealId).isAppeal_result()){
                int degree=sellerToBuyerAppealService.queryResult(appealId).getDamage_degree();
                // 余额返还
                int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
                moneyService.returnSellerAppealMoney(billId,billService.selectBill(billId).getBuyerId(),degree);
                moneyService.unfrozenUser(billService.selectBill(billId).getSellerId());

                //从黑名单内删除

                blacklistService.deleteReport(blacklistService.queryBlackId(billService.selectBill(billId).getBuyerId(),billService.selectBill(billId).getBuyerId()));
                //通知
                AppealMessageBean appealMessageBean=sellerToBuyerAppealService.message(appealId);
                messageController.appealRemove(appealMessageBean);
            }
            sellerToBuyerAppealService.deleteAppeal(appealId);
            return result.gainDeleteSuccess();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }

            if(sellerToBuyerAppealService.queryResult(appealId).isAppeal_result()){
                int degree=sellerToBuyerAppealService.queryResult(appealId).getDamage_degree();
                // 余额返还
                int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
                moneyService.returnSellerAppealMoney(billId,billService.selectBill(billId).getBuyerId(),degree);
                moneyService.unfrozenUser(billService.selectBill(billId).getSellerId());

                //从黑名单内删除

                blacklistService.deleteReport(blacklistService.queryBlackId(details.getUserId(),billService.selectBill(billId).getBuyerId()));
                //通知
                AppealMessageBean appealMessageBean=sellerToBuyerAppealService.message(appealId);
                messageController.appealRemove(appealMessageBean);
            }
            sellerToBuyerAppealService.deleteAppeal(appealId);
            return result.gainDeleteSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看申诉结果
    * */
    @GetMapping("/query/audit/{appealId}")
    public String queryResult(@PathVariable int appealId,
                              @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return sellerToBuyerAppealService.queryResult(appealId).toString();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBill_id();
            int respondentId=billService.selectBill(billId).getBuyerId();
//            被申诉者和申诉者才能查看
            if (sellerToBuyerAppealService.queryMyId(appealId)!=details.getUserId()
                    && respondentId!=details.getUserId()){
                throw new ForbiddenException();
            }
            return sellerToBuyerAppealService.queryResult(appealId).toString();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 更新申诉
    * */
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
//            已经被审核了无法更改
            if(sellerToBuyerAppealService.queryResult(appealId).isChecked()){
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
