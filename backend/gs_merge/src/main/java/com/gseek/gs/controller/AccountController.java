package com.gseek.gs.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;
import com.gseek.gs.service.inter.MoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.FileNotFoundException;

/**
 * @author Phak
 * @since 2023/5/15-11:27
 */
@RestController
@Slf4j
@RequestMapping("/users/account")
public class AccountController {

    @Autowired
    @Qualifier("moneyServiceImpl")
    MoneyService moneyService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 充值
     *
     * */
    @PostMapping
    public String recharge(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                           @RequestBody RechargeDTO dto)
            throws IllegalBlockSizeException, BadPaddingException, AlipayApiException, JsonProcessingException {
        dto.perService();
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            dto.perService();
            return moneyService.recharge(details.getUserId(),dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    /**
     * 提现
     *
     * */
    @PatchMapping
    public String withdrawals(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                              @RequestBody WithdrawalDTO dto)
            throws IllegalBlockSizeException, BadPaddingException, AlipayApiException, JsonProcessingException, FileNotFoundException {

        dto.perService();

        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return moneyService.withdrawal(details.getUserId(),dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }


    /**
     * 获取钱包所有信息
     *
     *
     * */
    @GetMapping
    public String getWalletInformation(@CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws JsonProcessingException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return moneyService.getWalletInformation(details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }


}
