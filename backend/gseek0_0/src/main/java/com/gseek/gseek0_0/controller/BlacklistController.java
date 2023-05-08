package com.gseek.gseek0_0.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gseek.gseek0_0.pojo.Blacklist;
import com.gseek.gseek0_0.service.BlacklistService;
import com.gseek.gseek0_0.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RestController
@RequestMapping("/report")
public class BlacklistController {
    @Autowired
    BlacklistService blacklistService;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd/");
    //TODO： 改变存图片的路径，从temp改到其他文件夹
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
        Blacklist blacklist=new Blacklist(claimerId,respondentId,appealReason,realPath);
        System.out.println(blacklist);
        blacklistService.addReport(blacklist);

        return "ok";
    }
    @GetMapping("/query_audit/{blackId}")
    public String queryResult(@PathVariable int blackId){
        if(blacklistService.queryChecked(blackId)){
            System.out.println(blacklistService.queryResult(blackId));
        }
        else{System.out.println("未审查");}
        return "ok";
    }
    @GetMapping("/{blackId}")
    public String queryReport(@PathVariable int blackId){

        return blacklistService.queryReport(blackId).toString();
    }
    @DeleteMapping("/{blackId}")
    public String deleteReport(@PathVariable int blackId){
        Blacklist blacklist=blacklistService.queryReport(blackId);
//        订单内的举报人是否和想删除订单的人id相同
//        TODO 思考要不要改成过滤器
        int reportId=blacklist.getClaimer_id();
        //TODO 设user的全局变量，这里的1只是用于接口测试
        if(reportId==1){
            blacklistService.deleteReport(blackId);
        }
        return "成功删除举报";
    }
    @PutMapping("/{blackId}")
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
        Blacklist blacklist=new Blacklist(appealReason,blackId,realPath);
        System.out.println(blacklist);
        blacklistService.updateReport(blacklist);

        return "ok";
    }

}
