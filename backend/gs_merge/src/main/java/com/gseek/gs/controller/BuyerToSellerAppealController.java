package com.gseek.gs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;
import com.gseek.gs.service.inter.MoneyService;
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
 * 买家申诉卖家
 * @author: Isabella
 * @create: 2023-05-12 23:52
 **/
@RestController
@RequestMapping("/after_sale/buyer")
@Slf4j
public class BuyerToSellerAppealController {
    @Autowired
    BuyerToSellerAppealService buyerToSellerAppealService;
    @Autowired
    BillMapper billMapper;
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MoneyService moneyService;
    @Autowired
    BillService billService;
    @Autowired
    MessageController messageController;
    @Autowired
    Result result;
    /**
     * 新增申诉
     * */
    @PostMapping("/complain")
    public String addBuyerToSellerAppeal(@RequestParam("provePic") MultipartFile provePic, HttpServletRequest request,
                                         @RequestParam int billId,@RequestParam String appealReason,@RequestParam int myId,
                                         @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            String realPath= FileUtils.fileUtil(provePic,request);
//            添加申诉
            BuyerToSellerAppealDTO buyerToSellerAppealDTO=new BuyerToSellerAppealDTO(appealReason,realPath,billId,myId);
            buyerToSellerAppealService.addBuyerToSellerAppeal(buyerToSellerAppealDTO,billId);
            return result.gainPostSuccess();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看申诉
    * */
    @GetMapping("/complain/{appealId}")
    public String queryAppeal(@PathVariable int appealId,
                                             @CurrentSecurityContext(expression = "authentication ") Authentication authentication){
//        管理员直接看
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            return buyerToSellerAppealService.queryAppeal(appealId).toString();
        }

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBillId();
            int respondentId=billService.selectBill(billId).getSellerId();
//            申诉人和被申诉人能看
            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()
            && respondentId!=details.getUserId()){
                throw new ForbiddenException();
            }
            return buyerToSellerAppealService.queryAppeal(appealId).toString();

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
            buyerToSellerAppealService.deleteAppeal(appealId);
            return result.gainDeleteSuccess();
        }

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            //只有本人能删除
            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            buyerToSellerAppealService.deleteAppeal(appealId);
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
//      管理员直接看
            return buyerToSellerAppealService.queryResult(appealId).toString();

        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            int billId=buyerToSellerAppealService.queryAppeal(appealId).getBillId();
            int respondentId=billService.selectBill(billId).getSellerId();
//            只有申诉者和被申诉者能看
            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()
                    && respondentId!=details.getUserId()){
                throw new ForbiddenException();
            }
            return buyerToSellerAppealService.queryResult(appealId).toString();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 更新申诉
     * */
    @PatchMapping("/complain/{appealId}")
    public String updateAppeal(@PathVariable int appealId,@RequestParam int billId,HttpServletRequest request,
                               @RequestParam String appealReason,@RequestParam("provePic") MultipartFile provePic,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
//          只有本人能够更改
            if (buyerToSellerAppealService.queryMyId(appealId)!=details.getUserId()){
                throw new ForbiddenException();
            }
            String realPath=FileUtils.fileUtil(provePic,request);
            int myId=details.getUserId();
            BuyerToSellerAppealDTO buyerToSellerAppealDTO=new BuyerToSellerAppealDTO(appealReason,realPath,billId,myId);
//          被审核就不能改了
            if(buyerToSellerAppealService.queryResult(appealId).isChecked()){
                throw new ServerException("已被审核，无法更改");
            }
            buyerToSellerAppealService.updateAppeal(buyerToSellerAppealDTO);
            return result.gainPatchSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("已被审核，无法更改");
        }


    }
}
