package com.gseek.gs.controller;


import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;
import com.gseek.gs.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-12 23:52
 **/
@RestController
@RequestMapping("/after_sale/buyer")
public class BuyerToSellerAppealController {
    //TODO 图片名字也得放进来啊
    @Autowired
    BuyerToSellerAppealService buyerToSellerAppealService;
    @PostMapping("/complain")
    public String addBuyerToSellerAppeal(@RequestParam("provePic") MultipartFile provePic, HttpServletRequest request,
                                         @RequestParam int billId,@RequestParam String appealReason,@RequestParam int myId){
        String realPath= FileUtils.fileUtil(provePic,request);
        BuyerToSellerAppealDTO buyerToSellerAppealDTO=new BuyerToSellerAppealDTO(appealReason,realPath,billId,myId);
        buyerToSellerAppealService.addBuyerToSellerAppeal(buyerToSellerAppealDTO);
        return "ok";
    }
    @GetMapping("/complain/{appealId}")
    public BuyerToSellerAppealBO queryAppeal(@PathVariable int appealId){
        return buyerToSellerAppealService.queryAppeal(appealId);
    }
    @DeleteMapping("/complain/{appealId}")
    public String deleteAppeal(@PathVariable int appealId,@RequestParam int myId){
        buyerToSellerAppealService.deleteAppeal(appealId,myId);
        return "ok";
    }
    @GetMapping("/query/audit/{appealId}")
    public BuyerToSellerAppealResultBO queryResult(@PathVariable int appealId){

        return buyerToSellerAppealService.queryResult(appealId);
    }
    //TODO 方法不support，要换（？
    @PatchMapping("/{appealId}")
    public String updateAppeal(@PathVariable int appealId,@RequestParam int billId,HttpServletRequest request,
                               @RequestParam String appealReason,@RequestParam("provePic") MultipartFile provePic){
        Map map=new HashMap<>();
        map.put("appealId",appealId);
        String realPath=FileUtils.fileUtil(provePic,request);
        if (billId!=0){map.put("billId",billId);}
        if (!appealReason.equals(null)){map.put("appealReason",appealReason);}
        //TODO 这边是不是null得确认一下
        if(!realPath.equals(null)){map.put("provePic",realPath);}
        buyerToSellerAppealService.updateAppeal(map);
        return "ok";
    }
}
