package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
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

import java.util.Objects;

/**
 * 处理交易操作.
 *
 * @author Phak
 * @since 2023/5/11-23:25
 */
@RestController
@Slf4j
@RequestMapping("/trade")
public class TradeController implements Controller{

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("billServiceImpl")
    BillService billService;

    /**
     * 查看订单情况
     * */
    @GetMapping("/bills/{bill_id}")
    public String getBillState(@PathVariable("bill_id") String billId )
            throws ServerException, JsonProcessingException {
        // 验参
        if (billId==null || billId.isEmpty()){
            throw new ParameterWrongException(
                    new ParameterWrongBean().addParameters("bill_id", billId)
            );
        }
        return billService.getBillState(PasswordUtil.decrypt(billId));
    }
    /**
     * 买家发起订单
     * */
    @PostMapping("/bills")
    public String postBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                           @RequestBody PostBillsDTO dto)
            throws ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);

        dto.perService();
        // 鉴权
        if (! Objects.equals(details.getUserId(),dto.getBuyerId()) ) {
            throw ForbiddenException.gainNotAccess();
        }

        return billService.postBill(dto);
    }
    /**
     * 买家支付订单
     * */
    @PutMapping("/bills")
    public String putPayBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                             @RequestBody PayBillDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();

        return billService.payBill(dto,details.getUserId());
    }

    /**
     * 卖家是否决定交货
     * */
    @PatchMapping("/bills")
    public String patchDeliveryBill(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                    @RequestBody PatchDeliveryBillDTO dto)
            throws ServerException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();

        return billService.deliveryBill(dto,details.getUserId());
    }

    /**
     * 卖家是否取消交易
     * */
    @PatchMapping("/bills/cancel")
    public String patchBillCancel(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                  @RequestBody PatchBillCancelDTO dto)
            throws ServerException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();

        return billService.patchBillCancel(dto, details.getUserId());
    }

    /**
     * 买家验货
     * */
    @PatchMapping("/inspect")
    public String patchBillInspect(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                   @RequestBody PatchBillInspectDTO dto)
            throws ServerException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();

        return billService.patchBillInspect(dto, details.getUserId());
    }

}
