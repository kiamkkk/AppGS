package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.BlacklistMapper;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.AdminBlacklistDTO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对应/report/**的操作.
 *
 * @author Isabella
 * @since 2023/5/6-20:06
 */
@Slf4j
@Service("blacklistServiceImpl")
public class BlacklistServiceImpl implements BlacklistService {
    @Autowired
    private BlacklistMapper blacklistMapper;
    @Autowired
    Result result;
    @Autowired
    ObjectMapper objectMapper;

    public void setBlacklistMapper(BlacklistMapper blacklistMapper){
        this.blacklistMapper=blacklistMapper;
    }
    public int addReport(BlacklistDTO blacklistDTO) {
        return blacklistMapper.addReport(blacklistDTO);
    }

    @Override
    public BlacklistResultBO queryResult(int blackId) {
        return blacklistMapper.queryResult(blackId);
    }



    public boolean queryChecked(int blackId){return blacklistMapper.queryChecked(blackId);}


    public BlacklistBO queryReport(int blackId) throws JsonProcessingException {

        return blacklistMapper.queryReport(blackId);
    }


    public int deleteReport(int blackId) {
            return blacklistMapper.deleteReport(blackId);
    }


    public int updateReport(BlacklistDO blacklistDO) {
        if(!blacklistDO.isChecked()){
            return blacklistMapper.updateReport(blacklistDO);
        }
        else {
            return 0;
        }

    }

    public List<BlacklistDO> queryAllUnchecked(){
        return blacklistMapper.queryAllUnchecked();
    }
    public int auditReport(BlacklistResultBO blacklistResultBO) {
        return blacklistMapper.auditReport(blacklistResultBO);
    }
    public int updateCheck(int blackId){return blacklistMapper.updateCheck(blackId);}

    public int updateAuditResult(BlacklistDO blacklistDO) {
        return blacklistMapper.updateAuditResult(blacklistDO);
    }
    public boolean queryByRespondentId(int respondentId){
        return blacklistMapper.queryByRespondentId(respondentId);
    }

    public int queryBlackId(int claimerId,int respondentId){
        return blacklistMapper.queryBlackId(claimerId,respondentId);
    }
}
