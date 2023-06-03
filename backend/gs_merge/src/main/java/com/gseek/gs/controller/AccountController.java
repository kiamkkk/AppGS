package com.gseek.gs.controller;

import com.alipay.api.AlipayApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.BusinessException;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;
import com.gseek.gs.service.inter.MoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

/**
 * 处理账户信息.
 *
 * @author Phak
 * @since 2023/5/15-11:27
 */
@RestController
@Slf4j
@RequestMapping("/users/account")
public class AccountController implements Controller{

    @Autowired
    @Qualifier("moneyServiceImpl")
    MoneyService moneyService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 充值
     * */
    @PostMapping
    public String recharge(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                           @RequestBody RechargeDTO dto)
            throws BusinessException, ServerException, AlipayApiException, JsonProcessingException {
        CustomWebAuthenticationDetails details=perService(authentication);
        dto.perService();

        return moneyService.recharge(details.getUserId(),dto);
    }

    /**
     * 提现
     * */
    @PatchMapping
    public String withdrawals(@CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                              @RequestBody WithdrawalDTO dto)
            throws BusinessException, ServerException, AlipayApiException,
                    JsonProcessingException, FileNotFoundException {
        CustomWebAuthenticationDetails details=perService(authentication);
        dto.perService();

        return moneyService.withdrawal(details.getUserId(),dto);
    }

    /**
     * 获取钱包所有信息
     * */
    @GetMapping
    public String getWalletInformation(@CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws JsonProcessingException {
        CustomWebAuthenticationDetails details=perService(authentication);

        return moneyService.getWalletInformation(details.getUserId());
    }

}
