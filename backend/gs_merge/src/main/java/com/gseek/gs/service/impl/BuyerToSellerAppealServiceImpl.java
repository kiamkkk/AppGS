package com.gseek.gs.service.impl;

import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.BuyerToSellerAppealMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;

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

    public void setBuyerToSellerAppealMapper(BuyerToSellerAppealMapper buyerToSellerAppealMapper) {
        this.buyerToSellerAppealMapper = buyerToSellerAppealMapper;
    }


    public int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO) {
        return buyerToSellerAppealMapper.addBuyerToSellerAppeal(buyerToSellerAppealDTO);
    }


    public BuyerToSellerAppealBO queryAppeal(int appealId) {
        return buyerToSellerAppealMapper.queryAppeal(appealId);
    }


    public int deleteAppeal(int appealId) {
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
        int billId=buyerToSellerAppealMapper.queryAppeal(appealId).getBill_id();
        int toUserId=billMapper.selectBillByBillId(billId).getSellerId();
        int goodId=billMapper.selectBillByBillId(billId).getGoodId();
        String goodName=goodMapper.selectGoodByGoodIdFully(goodId).getGoodName();
        AppealMessageBean appealMessageBean=new AppealMessageBean(toUserId,goodName,billId);
        return appealMessageBean;
    }
}
