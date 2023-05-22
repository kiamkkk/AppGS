package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.pojo.dto.*;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.Objects;

/**
 * @author Phak
 * @since 2023/5/11-23:25
 */
@RestController
@Slf4j
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("billServiceImpl")
    BillService billService;


    @PostMapping("/bills")
    public String postBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                           @RequestBody PostBillsDTO dto)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {

        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (! Objects.equals(details.getUserId(),dto.getBuyerId()) ) {
                throw new ForbiddenException();
            }

            return billService.postBill(dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @PutMapping("/bills")
    public String putPayBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             @RequestBody PayBillDTO dto)
            throws JsonProcessingException, ForbiddenException, IllegalBlockSizeException, BadPaddingException {

        dto.perService();

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return billService.payBill(dto,details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @PatchMapping("/bills")
    public String patchDeliveryBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                    @RequestBody PatchDeliveryBillDTO dto)
            throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return billService.deliveryBill(dto,details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @GetMapping("/bills/{bill_id}")
    public String getBillState(@PathVariable("bill_id") String billId )
            throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        // 验参
        if (billId==null || billId.isEmpty()){
            throw new ParameterWrongException(
                    new ParameterWrongBean().addParameters("bill_id", billId)
            );
        }
        return billService.getBillState(PasswordUtil.decrypt(billId));
    }

    @PatchMapping("/bills/cancel")
    public String patchBillCancel(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                  @RequestBody PatchBillCancelDTO dto)
            throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return billService.patchBillCancel(dto, details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @PatchMapping("/inspect")
    public String patchBillInspect(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                   PatchBillInspectDTO dto)
            throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return billService.patchBillInspect(dto, details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
}
