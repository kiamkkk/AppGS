package com.gseek.gs.service.impl;

import com.gseek.gs.dao.BuyerToSellerAppealMapper;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import com.gseek.gs.service.inter.BuyerToSellerAppealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-12 21:34
 **/
@Service
public class BuyerToSellerAppealServiceImpl implements BuyerToSellerAppealService {

    @Autowired
    BuyerToSellerAppealMapper buyerToSellerAppealMapper;

    public void setBuyerToSellerAppealMapper(BuyerToSellerAppealMapper buyerToSellerAppealMapper) {
        this.buyerToSellerAppealMapper = buyerToSellerAppealMapper;
    }


    public int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO) {
        return buyerToSellerAppealMapper.addBuyerToSellerAppeal(buyerToSellerAppealDTO);
    }


    public BuyerToSellerAppealBO queryAppeal(int appealId) {
        return buyerToSellerAppealMapper.queryAppeal(appealId);
    }


    public int deleteAppeal(int appealId, int myId) {
        if(myId==buyerToSellerAppealMapper.queryMyId(appealId)){
            return buyerToSellerAppealMapper.deleteAppeal(appealId);}
       return 0;
    }


    public BuyerToSellerAppealResultBO queryResult(int appealId) {
        return buyerToSellerAppealMapper.queryResult(appealId);
    }


    public int updateAppeal(Map map) {
        return buyerToSellerAppealMapper.updateAppeal(map);
    }


    public int queryMyId(int appealId) {
        return buyerToSellerAppealMapper.queryMyId(appealId);
    }
}
