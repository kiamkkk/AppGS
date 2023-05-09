package com.gseek.gs.controller;

import com.gseek.gs.pojo.data.Blacklist;
import com.gseek.gs.service.inter.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

/**
 * 对应 /admin/** 下的操作
 *
 * @author Isabella
 * @since 2023/5/7-21:30
 */
@RestController
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    BlacklistService blacklistService;
    //TODO 过滤器
    @GetMapping("/audit/report/unChecked")
    public List<Blacklist> queryAllUnchecked(){
        return blacklistService.queryAllUnchecked();
    }
//TODO 外面的值穿不进来（？
    @PutMapping("/audit/report/{blackId}")
    public String auditReport(@PathVariable int blackId,@RequestBody Blacklist blacklist){
        HashMap map = new HashMap();
        int adminId=blacklist.getAdmin_id();
        System.out.println(adminId);
        boolean appealResult=blacklist.isAppeal_result();
        String disapproveReason=blacklist.getDisapprove_reason();
        map.put("admin_id",adminId);
        map.put("appeal_result",appealResult);
        map.put("disapprove_reason",disapproveReason);
        map.put("black_id",blackId);
        blacklistService.auditReport(map);
        blacklistService.updateCheck(blackId);
        return "审核成功";
    }

}
