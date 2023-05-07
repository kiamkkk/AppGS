package com.gseek.gseek0_0.controller;

import com.gseek.gseek0_0.pojo.Blacklist;
import com.gseek.gseek0_0.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;

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
