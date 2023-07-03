package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.BuyerToSellerAppealMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.NoticeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Isabella
 * @create: 2023-05-12 21:34
 **/
@Service
public class BuyerToSellerAppealServiceImpl implements BuyerToSellerAppealService {

    @Autowired
    BuyerToSellerAppealMapper buyerToSellerAppealMapper;
    @Autowired
    BillMapper billMapper;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MoneyService moneyService;
    @Autowired
    MessageController messageController;
    @Autowired
    BillService billService;
    @Autowired
    BlacklistService blacklistService;

    public void setBuyerToSellerAppealMapper(BuyerToSellerAppealMapper buyerToSellerAppealMapper) {
        this.buyerToSellerAppealMapper = buyerToSellerAppealMapper;
    }


    public int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO,int billId) throws JsonProcessingException {
        int respondentId=billMapper.selectBillByBillId(billId).getSellerId();
//            暂时冻结被申诉人账号
        moneyService.frozenUser(respondentId);
//            通知被申诉人
        NoticeMessage noticeMessage=new NoticeMessage("您的账户由于被申诉已被暂时冻结",System.currentTimeMillis(),respondentId);
        messageController.general(noticeMessage);
        return buyerToSellerAppealMapper.addBuyerToSellerAppeal(buyerToSellerAppealDTO);
    }


    public BuyerToSellerAppealBO queryAppeal(int appealId) {
        return buyerToSellerAppealMapper.queryAppeal(appealId);
    }


    public int deleteAppeal(int appealId) throws JsonProcessingException {
        //          如果已经被审核
        if(queryResult(appealId).isAppealResult()){
            int billId=queryAppeal(appealId).getBillId();
//                余额返还
            moneyService.returnBuyerAppealMoney(billId,billService.selectBill(billId).getSellerId());
//              账户解冻
            moneyService.unfrozenUser(billService.selectBill(billId).getSellerId());
            //从黑名单内删除
            blacklistService.deleteReport(blacklistService.queryBlackId(billService.selectBill(billId).getBuyerId(),billService.selectBill(billId).getSellerId()));
            //通知
            AppealMessageBean appealMessageBean=message(appealId);
            messageController.appealRemove(appealMessageBean);
        }
            return buyerToSellerAppealMapper.deleteAppeal(appealId);
    }

    public BuyerToSellerAppealResultBO queryResult(int appealId) {
        return buyerToSellerAppealMapper.queryResult(appealId);
    }
    public int updateAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO) {
        return buyerToSellerAppealMapper.updateAppeal(buyerToSellerAppealDTO);
    }


    public int queryMyId(int appealId) {
        return buyerToSellerAppealMapper.queryMyId(appealId);
    }
    public AppealMessageBean message(int appealId){
        int billId=buyerToSellerAppealMapper.queryAppeal(appealId).getBillId();
        int toUserId=billMapper.selectBillByBillId(billId).getSellerId();
        int goodId=billMapper.selectBillByBillId(billId).getGoodId();
        String goodName=goodMapper.selectGoodByGoodIdFully(goodId).getGoodName();
        AppealMessageBean appealMessageBean=new AppealMessageBean(toUserId,goodName,billId);
        return appealMessageBean;
    }
}
