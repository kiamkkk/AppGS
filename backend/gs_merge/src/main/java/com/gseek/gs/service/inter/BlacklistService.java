package com.gseek.gs.service.inter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
import com.gseek.gs.pojo.dto.BlacklistDTO;

import java.util.List;
import java.util.Map;

/**
 * 对应/report/**的操作.
 *
 * @author Isabella
 * @since 2023/5/6-20:05
 */

public interface BlacklistService {
    int addReport(BlacklistDTO blacklistDTO);
    BlacklistResultBO queryResult(int blackId);
    boolean queryChecked(int blackId);
    BlacklistBO queryReport(int blackId) throws JsonProcessingException;
    int deleteReport(int blackId);
    //    int userId===claimer_id,int blackId,String appeal_reason
    int updateReport(BlacklistDO blacklistDO);
    List<BlacklistDO> queryAllUnchecked();
    int auditReport(AdminBlacklistDTO adminBlacklistDTO);
    int updateCheck(int blackId);
    //    int adminId,boolean appealResult,int blackId
    int updateAuditResult(BlacklistDO blacklistDO);
    boolean queryByRespondentId(int respondentId);
}
