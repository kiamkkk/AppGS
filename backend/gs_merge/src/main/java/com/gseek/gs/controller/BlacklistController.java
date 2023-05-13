package com.gseek.gs.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 对应 /report/** 下的操作
 *
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
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/");
    //TODO： 改变存图片的路径，从temp改到其他文件夹&&把处理文件的弄到一个工具类里面去
    @PostMapping("/")
    public String addReport(@RequestParam("provePic") MultipartFile provePic, @RequestParam int respondentId,
                            @RequestParam String appealReason, @RequestParam int claimerId,
                            HttpServletRequest request) throws IOException {
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        String format=sdf.format(new Date());
        String filePath="";
        File folder=new File(realPath+format);
        if(!folder.isDirectory()){
            folder.mkdirs();
            String oldName=provePic.getOriginalFilename();
            String newName= UUID.randomUUID().toString()+
                    oldName.substring(oldName.lastIndexOf("."),oldName.length());
            try{
                provePic.transferTo(new File(folder,newName));
                filePath=request.getScheme()+"://"+request.getServerName()+":"+
                        request.getServerPort()+"/uploadFile"+format+newName;
            }catch (IOException e){
                e.printStackTrace();
                return "上传失败";
            }
        }
        System.out.println(realPath);
        //保存到数据库
        BlacklistDTO blacklistDTO =new BlacklistDTO(claimerId,respondentId,appealReason,realPath);
        System.out.println(blacklistDTO);
        blacklistService.addReport(blacklistDTO);

        return result.gainPostSuccess();
    }
    @GetMapping("/query_audit/{blackId}")
    public BlacklistResultBO queryResult(@PathVariable int blackId){
        return blacklistService.queryResult(blackId);
    }
    @GetMapping("/{blackId}")
    public BlacklistBO queryReport(@PathVariable int blackId){
        return blacklistService.queryReport(blackId);
    }
    @DeleteMapping("/{blackId}")
    public String deleteReport(@PathVariable int blackId) throws JsonProcessingException {
        BlacklistBO blacklistBO =blacklistService.queryReport(blackId);
//        订单内的举报人是否和想删除订单的人id相同
//        TODO 思考要不要改成过滤器
        int reportId= blacklistBO.getClaimer_id();
        //TODO 设user的全局变量，这里的1只是用于接口测试
        if(reportId==1&&blacklistService.queryChecked(blackId)){
            blacklistService.deleteReport(blackId);
        }
        return result.gainDeleteSuccess();
    }
    @PatchMapping("/{blackId}")
//    TODO 缺一个验证用户身份的
    public String updateReport(@RequestParam("provePic") MultipartFile provePic,@PathVariable int blackId,
                               @RequestParam String appealReason,
                               HttpServletRequest request) throws IOException {
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        String format=sdf.format(new Date());
        String filePath="";
        File folder=new File(realPath+format);
        if(!folder.isDirectory()){
            folder.mkdirs();
            String oldName=provePic.getOriginalFilename();
            String newName= UUID.randomUUID().toString()+
                    oldName.substring(oldName.lastIndexOf("."),oldName.length());
            try{
                provePic.transferTo(new File(folder,newName));
                filePath=request.getScheme()+"://"+request.getServerName()+":"+
                        request.getServerPort()+"/uploadFile"+format+newName;
            }catch (IOException e){
                e.printStackTrace();
                return "上传失败";
            }
        }
        System.out.println(realPath);
        //保存到数据库
        BlacklistDO blacklistDO =new BlacklistDO(appealReason,blackId,realPath);
        System.out.println(blacklistDO);
        blacklistService.updateReport(blacklistDO);

        return result.gainPatchSuccess();
    }

}
