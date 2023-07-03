package com.gseek.gs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.config.login.handler.admin.AdminWebAuthenticationDetails;
import com.gseek.gs.dao.BlacklistMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.util.FileUtils;
import com.gseek.gs.websocket.controller.MessageController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 对应 /report/** 下的操作
 *黑名单功能
 * @author Isabella
 * @since 2023/5/6-20:30
 */
@Controller
@RestController
@RequestMapping("/report")
@Slf4j
public class BlacklistController {
    @Autowired
    BlacklistService blacklistService;
    @Autowired
    Result result;
    @Autowired
    MessageController messageController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BlacklistMapper blacklistMapper;

//    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/");
    /**
     * 添加黑名单举报
     * */
    @PostMapping("/")
    public String addReport(@RequestParam("provePic") MultipartFile provePic, @RequestParam int respondentId,
                            @RequestParam String appealReason, @RequestParam int claimerId,
                            HttpServletRequest request, @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws IOException {
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (claimerId!=details.getUserId()){
                throw new ForbiddenException();
            }
            String realPath = FileUtils.fileUtil(provePic,request);
            //保存到数据库
            BlacklistDTO blacklistDTO =new BlacklistDTO(claimerId,respondentId,appealReason,realPath);
            System.out.println(blacklistDTO);
            blacklistService.addReport(blacklistDTO);
            return result.gainPostSuccess();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看黑名单举报结果
     * */
    @GetMapping("/query_audit/{blackId}")
    public String queryResult(@PathVariable int blackId,
                                         @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
//            管理员直接查看
            return objectMapper.writeValueAsString(blacklistService.queryResult(blackId));
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
//            只有用户本人可以查看
            if (blacklistMapper.queryReport(blackId).getClaimerId()!=details.getUserId()){
                throw new ForbiddenException();
            }
            return objectMapper.writeValueAsString(blacklistService.queryResult(blackId));
        } else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 查看黑名单举报
     * */
    @GetMapping("/{blackId}")
    public String queryReport(@PathVariable int blackId,
                              @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
//            管理员直接查看
            return objectMapper.writeValueAsString(blacklistService.queryReport(blackId));
        }
//        只有举报人和被举报人能查看
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (blacklistMapper.queryReport(blackId).getClaimerId()!=details.getUserId()
            ||blacklistMapper.queryReport(blackId).getRespondentId()!=details.getUserId()){
                throw new ForbiddenException();
            }
            return objectMapper.writeValueAsString(blacklistService.queryReport(blackId));
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }
    /**
     * 删除黑名单举报
     * */
    @DeleteMapping("/{blackId}")
    public String deleteReport(@PathVariable int blackId,
                               @CurrentSecurityContext(expression = "authentication ") Authentication authentication) throws JsonProcessingException {
//      管理员可以直接删除
        if(authentication.getDetails() instanceof AdminWebAuthenticationDetails adminDetails) {
            blacklistService.deleteReport(blackId);
            return result.gainDeleteSuccess();
        }
//        只有申诉者可以删除订单
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (blacklistMapper.queryReport(blackId).getClaimerId()!=details.getUserId()){
                throw new ForbiddenException();
            }
            blacklistService.deleteReport(blackId);
            return result.gainDeleteSuccess();
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }


    }
    /**
     * 更新黑名单举报
     * */
    @PatchMapping("/{blackId}")
    public String updateReport(@RequestParam("provePic") MultipartFile provePic,@PathVariable int blackId,
                               @RequestParam String appealReason,@CurrentSecurityContext(expression = "authentication ") Authentication authentication,
                               HttpServletRequest request) throws IOException {
//        只有申诉人本人能更新
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            if (blacklistMapper.queryReport(blackId).getClaimerId()!=details.getUserId()){
                throw new ForbiddenException();
            }
            String realPath=FileUtils.fileUtil(provePic,request);
            BlacklistDO blacklistDO =new BlacklistDO(appealReason,blackId,realPath);
            blacklistService.updateReport(blacklistDO);
            return result.gainPatchSuccess();

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("已经被审核，无法更改");
        }


    }

}
