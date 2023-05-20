package com.gseek.gs.service.impl;

import com.gseek.gs.dao.SellerToBuyerAppealMapper;
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
}
