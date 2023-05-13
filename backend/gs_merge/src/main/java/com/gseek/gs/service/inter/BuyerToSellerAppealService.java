package com.gseek.gs.service.inter;



import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;

import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-12 21:33
 **/
public interface BuyerToSellerAppealService {
    int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO);
    BuyerToSellerAppealBO queryAppeal(int appealId);
    int deleteAppeal(int appealId,int myId);
    BuyerToSellerAppealResultBO queryResult(int appealId);
    int updateAppeal(Map map);
    int queryMyId(int appealId);
}
