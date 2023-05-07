package com.gseek.gseek0_0.service;


import com.gseek.gseek0_0.pojo.Blacklist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BlacklistService {
    int addReport(Blacklist blacklist);
    boolean queryResult(int blackId);
    boolean queryChecked(int blackId);
    Blacklist queryReport(int blackId);
    int deleteReport(int blackId);
    //    int userId===claimer_id,int blackId,String appeal_reason
    int updateReport(Blacklist blacklist);
    List<Blacklist> queryAllUnchecked();
    int auditReport(Map map);
    int updateCheck(int blackId);
    //    int adminId,boolean appealResult,int blackId
    int updateAuditResult(Blacklist blacklist);
}
