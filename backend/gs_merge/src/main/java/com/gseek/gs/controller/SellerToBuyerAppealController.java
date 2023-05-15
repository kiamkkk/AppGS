package com.gseek.gs.controller;


import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;
import com.gseek.gs.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-13 23:35
 **/
@RestController
@RequestMapping("/after_sale/seller")
public class SellerToBuyerAppealController {
    //TODO 和buyer的controller一样的TODO也要改
    //TODO 图片名字也得放进来啊
    @Autowired
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @PostMapping("/complain")
    public String addSellerToBuyerAppeal(@RequestParam("pic_before") MultipartFile picBefore,@RequestParam("pic_after") MultipartFile picAfter, HttpServletRequest request,
                                         @RequestParam int billId, @RequestParam String appealReason, @RequestParam int myId,@RequestParam boolean accept){
        String pathBefore= FileUtils.fileUtil(picBefore,request);
        String pathAfter=FileUtils.fileUtil(picAfter,request);
        SellerToBuyerAppealDTO sellerToBuyerAppealDTO=new SellerToBuyerAppealDTO(appealReason,pathBefore,pathAfter,accept,billId,myId);
        sellerToBuyerAppealService.addSellerToBuyerAppeal(sellerToBuyerAppealDTO);
        return "ok";
    }
    @GetMapping("/complain/{appealId}")
    public SellerToBuyerAppealBO queryAppeal(@PathVariable int appealId){
        return sellerToBuyerAppealService.queryAppeal(appealId);
    }
    @DeleteMapping("/complain/{appealId}")
    public String deleteAppeal(@PathVariable int appealId,@RequestParam int myId){
        sellerToBuyerAppealService.deleteAppeal(appealId,myId);
        return "ok";
    }
    @GetMapping("/query/audit/{appealId}")
    public SellerToBuyerAppealResultBO queryResult(@PathVariable int appealId){
        return sellerToBuyerAppealService.queryResult(appealId);
    }
    @PatchMapping("/{appealId}")
    public String updateAppeal(@PathVariable int appealId,@RequestParam("pic_before") MultipartFile picBefore,@RequestParam("pic_after") MultipartFile picAfter, HttpServletRequest request,
                               @RequestParam int billId, @RequestParam String appealReason, @RequestParam int myId,@RequestParam boolean accept){
        Map map=new HashMap<>();
        map.put("appealId",appealId);
        String pathBefore= FileUtils.fileUtil(picBefore,request);
        String pathAfter=FileUtils.fileUtil(picAfter,request);
        if (billId!=0){map.put("billId",billId);}
        if (!appealReason.equals(null)){map.put("appealReason",appealReason);}
        //TODO 这边是不是null得确认一下
        if(!pathBefore.equals(null)){map.put("picBefore",pathBefore);}
        if(!pathAfter.equals(null)){map.put("picAfter",pathAfter);}
        if (accept==false){map.put("accept",accept);}
        sellerToBuyerAppealService.updateAppeal(map);
        return "ok";
    }
}
