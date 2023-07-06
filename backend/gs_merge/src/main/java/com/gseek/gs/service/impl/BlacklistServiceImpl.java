package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.BlacklistMapper;
import com.gseek.gs.dao.UserPasswordMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.appeal.AlreadyAuditedException;
import com.gseek.gs.pojo.bean.BlacklistBean;
import com.gseek.gs.pojo.business.BlacklistBO;
import com.gseek.gs.pojo.business.BlacklistResultBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.data.BlacklistDO;
import com.gseek.gs.pojo.dto.BlacklistDTO;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;
import com.gseek.gs.websocket.controller.MessageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    MessageController messageController;
    @Autowired
    @Lazy
    BuyerToSellerAppealService buyerToSellerAppealService;
    @Autowired
    @Lazy
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @Autowired
    UserPasswordMapper userPasswordMapper;

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
        BlacklistBean blacklistBean=blacklistMapper.queryReport(blackId);
        int climerId=blacklistBean.getClaimerId();
        String claimerName=userPasswordMapper.selectUserNameByUserId(climerId);
        int respondentId=blacklistBean.getRespondentId();
        String respondentName=userPasswordMapper.selectUserNameByUserId(respondentId);
        BlacklistBO blacklistBO=new BlacklistBO(claimerName,respondentName,
                blacklistBean.getAppealReason(),blacklistBean.isChecked(),
                blacklistBean.getProvePic(),blacklistBean.getBlackId());
        return blacklistBO;
    }


    public int deleteReport(int blackId) throws JsonProcessingException {
        if(blacklistMapper.queryResult(blackId).isAppealResult()){
//              删除后通知被申诉人
            messageController.blacklistRemove(blacklistMapper.queryReport(blackId).getRespondentId());
        }
            return blacklistMapper.deleteReport(blackId);
    }


    public int updateReport(BlacklistDO blacklistDO) {
        boolean check=blacklistMapper.queryChecked(blacklistDO.getBlackId());
        if(!check){
            return blacklistMapper.updateReport(blacklistDO);
        }
        else {
            log.debug("已经被审核无法更改");
            throw new AlreadyAuditedException();
        }

    }

    public List<BlacklistBO> queryAllUnchecked(){
        return blacklistMapper.queryAllUnchecked();
    }
    public int auditReport(BlacklistResultBO blacklistResultBO, int blackId) throws JsonProcessingException {
        //如果被加入黑名单提醒用户
        if(blacklistResultBO.isAppealResult()){
            BlacklistBean blacklistBO=blacklistMapper.queryReport(blackId);
            messageController.blacklist(blacklistBO);
        }
        return blacklistMapper.auditReport(blacklistResultBO);
    }
    public int updateCheck(int blackId){return blacklistMapper.updateCheck(blackId);}

    public int updateAuditResult(BlacklistResultBO blacklistResultBO) {
        return blacklistMapper.auditReport(blacklistResultBO);
    }
    public Boolean queryByRespondentId(int respondentId){
        return blacklistMapper.queryByRespondentId(respondentId);
    }

    public int queryBlackId(int claimerId,int respondentId){
        return blacklistMapper.queryBlackId(claimerId,respondentId);
    }
    public int insertAuditedBlacklistByBuyer(int appealId,int respondentId,int adminId){
        BuyerToSellerAppealBO buyerToSellerAppealBO=buyerToSellerAppealService.queryAppeal(appealId);
//           拿到申诉人（买家）ID
        int claimerId =buyerToSellerAppealBO.getMyId();
        String appealReason=buyerToSellerAppealService.queryAppeal(appealId).getAppealReason();
        String provePic=buyerToSellerAppealService.queryAppeal(appealId).getProvePic();
        BlacklistDO blacklistDO=new BlacklistDO(claimerId,respondentId,appealReason,
                true,true,adminId,"",provePic);
        return blacklistMapper.insertAuditedBlacklist(blacklistDO);
    }
    public int insertAuditedBlacklistBySeller(int appealId,int respondentId,int adminId){
        int claimerId =sellerToBuyerAppealService.queryAppeal(appealId).getMyId();
        String appealReason=sellerToBuyerAppealService.queryAppeal(appealId).getAppealReason();
        String provePic=sellerToBuyerAppealService.queryAppeal(appealId).getPicAfter();
        BlacklistDO blacklistDO=new BlacklistDO(claimerId,respondentId,appealReason,
                true,true,adminId,"",provePic);
        return blacklistMapper.insertAuditedBlacklist(blacklistDO);
    }
}
