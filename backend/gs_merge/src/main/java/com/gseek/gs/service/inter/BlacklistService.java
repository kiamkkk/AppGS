package com.gseek.gs.service.inter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.bean.BlacklistBean;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;

import java.util.List;

/**
 * 对应/report/**的操作.
 *
 * @author Isabella
 * @since 2023/5/6-20:05
 */

public interface BlacklistService {
    /**
     *添加黑名单
     * */
    int addReport(BlacklistDTO blacklistDTO);
    /**
     *查看查询结果
     * */
    BlacklistResultBO queryResult(int blackId);
    /**
     *查看是否被审核过
     * */
    boolean queryChecked(int blackId);
    /**
     *查看黑名单
     * */
    BlacklistBO queryReport(int blackId) throws JsonProcessingException;
    /**
     *删除黑名单
     * */
    int deleteReport(int blackId) throws JsonProcessingException;
    /**
     *更新黑名单内容
     * */
    int updateReport(BlacklistDO blacklistDO);
    /**
     *查看所有未审核的黑名单
     * */
    List<BlacklistDO> queryAllUnchecked();
    /**
     *审核黑名单
     * */
    int auditReport(BlacklistResultBO blacklistResultBO,int blackId) throws JsonProcessingException;
    /**
     *设置为已经审核
     * */
    int updateCheck(int blackId);
    /**
     *更新审核结果
     * */
    int updateAuditResult(BlacklistResultBO blacklistResultBO);
    /**
     *通过被申诉人ID查看审核结果
     * */
    Boolean queryByRespondentId(int respondentId);
    /**
     *查看blackId
     * */
    int queryBlackId(int claimerId,int respondentId);
    /**
     *买家申诉时添加黑名单
     * */
    int insertAuditedBlacklistByBuyer(int appealId,int respondentId,int adminId);
    /**
     *卖家申诉时添加黑名单
     * */
    int insertAuditedBlacklistBySeller(int appealId,int respondentId,int adminId);
}
