package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Isabella
 * @since 2023/5/2-20:00
 */
@Repository
@Mapper
public interface BlacklistMapper {
    //TODO 改图片储存路径
    //TODO 传给前端的图片路径要加密
    //提交举报
    int addReport(BlacklistDTO blacklistDTO);
    //查询审核结果

    BlacklistResultBO queryResult(int blackId);
//    查询是否被审核
    //TODO 结果也要限制是否是本人
    boolean queryChecked(int blackId);
//    查看举报信息
    //TODO 把传回去的id换成用户名
    BlacklistBO queryReport(int blackId);
//    删除举报
    //TODO 要查看是否有被审核，有的话要从黑名单拉出来（？）不确定
    int deleteReport(int blackId);
//    修改举报，童谣要看看id是不是举报人id
    //TODO 这个也要看有没有被审核过！
//    int userId===claimer_id,int blackId,String appeal_reason
    int updateReport(BlacklistDO blacklistDO);
//    查询所有未审核举报
    List<BlacklistDO> queryAllUnchecked();
//    int adminId,boolean appealResult,int blackId,String disapproveReason
//    审核举报
    int auditReport(Map map);
    //改为已查看
    int updateCheck(int blackId);
//    int adminId,boolean appealResult,int blackId
    //TODO 这个好像没有必要，直接用auditReport就行（？）
    int updateAuditResult(BlacklistDO blacklistDO);


}
