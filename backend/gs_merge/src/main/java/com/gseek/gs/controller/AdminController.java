package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
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
    @Autowired
    Result result;
    //TODO 过滤器
    @GetMapping("/audit/report/unChecked")
    public List<BlacklistDO> queryAllUnchecked(){
        return blacklistService.queryAllUnchecked();
    }
//TODO 外面的值传不进来（？&&要不要用patch
    @PutMapping("/audit/report/{blackId}")
    public String auditReport(@PathVariable int blackId, @RequestBody AdminBlacklistDTO adminBlacklistDTO) throws JsonProcessingException {
        HashMap map = new HashMap();
        int adminId= adminBlacklistDTO.getAdmin_id();
        System.out.println(adminId);
        boolean appealResult= adminBlacklistDTO.isAppeal_result();
        String disapproveReason= adminBlacklistDTO.getDisapprove_reason();
        map.put("admin_id",adminId);
        map.put("appeal_result",appealResult);
        map.put("disapprove_reason",disapproveReason);
        map.put("black_id",blackId);
        blacklistService.auditReport(map);
        blacklistService.updateCheck(blackId);
        return result.gainPatchSuccess();
    }

}
