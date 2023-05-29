package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Isabella
 * @since 2023/5/2-20:00
 */
@Repository
@Mapper
public interface BlacklistMapper {
    /**
     *提交举报
     * */
    int addReport(@Param("blacklistDTO")BlacklistDTO blacklistDTO);
    /**
     *查询审核结果
     * */
    BlacklistResultBO queryResult(@Param("blackId")int blackId);
    /**
     *查询是否被审核
     * */
    boolean queryChecked(@Param("blackId")int blackId);
    /**
     *查看举报信息
     * */
    BlacklistBO queryReport(@Param("blackId")int blackId);
    /**
     *删除举报
     * */
    int deleteReport(@Param("blackId")int blackId);
    /**
     *修改举报
     * */
    int updateReport(@Param("blacklistDO")BlacklistDO blacklistDO);
    /**
     *查询所有未审核举报
     * */
    List<BlacklistDO> queryAllUnchecked();
    /**
     *审核举报
     * */
    int auditReport(@Param("blacklistResultBO")BlacklistResultBO blacklistResultBO);
    /**
     *改审核状态为已审核
     * */
    int updateCheck(@Param("blackId")int blackId);
    /**
     *更改审核结果
     * */
    int updateAuditResult(@Param("blacklistResultBO")BlacklistResultBO blacklistResultBO);
    /**
     *查询在不在黑名单内
     * */
    Boolean queryByRespondentId(@Param("respondentId")int respondentId);
    /**
     *查看黑名单ID
     * */
    //todo 有没有更好的查法
    int queryBlackId(@Param("claimerId")int claimerId,@Param("respondentId")int respondentId);
}
