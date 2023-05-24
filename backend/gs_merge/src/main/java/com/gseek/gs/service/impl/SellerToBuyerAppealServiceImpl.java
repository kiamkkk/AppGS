package com.gseek.gs.service.impl;

import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.SellerToBuyerAppealMapper;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import com.gseek.gs.service.inter.SellerToBuyerAppealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public void setSellerToBuyerAppealMapper(SellerToBuyerAppealMapper sellerToBuyerAppealMapper) {
        this.sellerToBuyerAppealMapper = sellerToBuyerAppealMapper;
    }


    public int addSellerToBuyerAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO) {
        return sellerToBuyerAppealMapper.addSellerToBuyerAppeal(sellerToBuyerAppealDTO);
    }


    public SellerToBuyerAppealBO queryAppeal(int appealId) {
        return sellerToBuyerAppealMapper.queryAppeal(appealId);
    }




    public int deleteAppeal(int appealId) {
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
