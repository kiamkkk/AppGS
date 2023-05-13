package com.gseek.gs.service.inter;


import com.gseek.gs.pojo.data.Blacklist;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * 对应/report/**的操作.
 *
 * @author Isabella
 * @since 2023/5/6-20:05
 */

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
