package com.gseek.gs.service.impl;

import com.gseek.gs.dao.BlacklistMapper;
import com.gseek.gs.pojo.data.Blacklist;

import com.gseek.gs.service.inter.BlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void setBlacklistMapper(BlacklistMapper blacklistMapper){
        this.blacklistMapper=blacklistMapper;
    }
    public int addReport(Blacklist blacklist) {
        return blacklistMapper.addReport(blacklist);
    }


    public boolean queryResult(int blackId) {
        return blacklistMapper.queryResult(blackId);
    }
    public boolean queryChecked(int blackId){return blacklistMapper.queryChecked(blackId);}


    public Blacklist queryReport(int blackId) {
        return blacklistMapper.queryReport(blackId);
    }


    public int deleteReport(int blackId) {
        return blacklistMapper.deleteReport(blackId);
    }


    public int updateReport(Blacklist blacklist) {
        return blacklistMapper.updateReport(blacklist);
    }

    public List<Blacklist> queryAllUnchecked(){
        return blacklistMapper.queryAllUnchecked();
    }
    public int auditReport(Map map) {
        return blacklistMapper.auditReport(map);
    }
    public int updateCheck(int blackId){return blacklistMapper.updateCheck(blackId);}

    public int updateAuditResult(Blacklist blacklist) {
        return blacklistMapper.updateAuditResult(blacklist);
    }
}
