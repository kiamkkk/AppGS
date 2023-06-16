package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.SellerToBuyerAppealMapper;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.BlacklistService;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;
import com.gseek.gs.websocket.controller.MessageController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author: Isabella
 * @create: 2023-05-13 15:34
 **/
@Service
public class SellerToBuyerAppealServiceImpl implements SellerToBuyerAppealService {
    @Autowired
    SellerToBuyerAppealMapper sellerToBuyerAppealMapper;
    @Autowired
    BillMapper billMapper;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MoneyService moneyService;
    @Autowired
    BillService billService;
    @Autowired
    @Lazy
    BlacklistService blacklistService;
    @Autowired
    MessageController messageController;

    public void setSellerToBuyerAppealMapper(SellerToBuyerAppealMapper sellerToBuyerAppealMapper) {
        this.sellerToBuyerAppealMapper = sellerToBuyerAppealMapper;
    }


    public int addSellerToBuyerAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO) {
        return sellerToBuyerAppealMapper.addSellerToBuyerAppeal(sellerToBuyerAppealDTO);
    }


    public SellerToBuyerAppealBO queryAppeal(int appealId) {
        return sellerToBuyerAppealMapper.queryAppeal(appealId);
    }




    public int deleteAppeal(int appealId) throws JsonProcessingException {
        //          如果申诉已经通过审核
        if(queryResult(appealId).isAppeal_result()){
            int degree=queryResult(appealId).getDamage_degree();
            // 余额返还
            int billId=queryAppeal(appealId).getBill_id();
            moneyService.returnSellerAppealMoney(billId,billService.selectBill(billId).getBuyerId(),degree);
            moneyService.unfrozenUser(billService.selectBill(billId).getSellerId());
            //从黑名单内删除
            blacklistService.deleteReport(blacklistService.queryBlackId(billService.selectBill(billId).getBuyerId(),billService.selectBill(billId).getBuyerId()));
            //通知
            AppealMessageBean appealMessageBean=message(appealId);
            messageController.appealRemove(appealMessageBean);
        }
            return sellerToBuyerAppealMapper.deleteAppeal(appealId);
    }


    public SellerToBuyerAppealResultBO queryResult(int appealId) {
        return sellerToBuyerAppealMapper.queryResult(appealId);
    }


    public int updateAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO) {
        return sellerToBuyerAppealMapper.updateAppeal(sellerToBuyerAppealDTO);
    }


    public int queryMyId(int appealId) {
        return sellerToBuyerAppealMapper.queryMyId(appealId);
    }
    public AppealMessageBean message(int appealId){
        int billId=sellerToBuyerAppealMapper.queryAppeal(appealId).getBill_id();
        int toUserId=billMapper.selectBillByBillId(billId).getBuyerId();
        int goodId=billMapper.selectBillByBillId(billId).getGoodId();
        String goodName=goodMapper.selectGoodByGoodIdFully(goodId).getGoodName();
        AppealMessageBean appealMessageBean=new AppealMessageBean(toUserId,goodName,billId);
        return appealMessageBean;
    }
}
