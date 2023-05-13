package com.gseek.gs.service.inter;


import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;

import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-13 15:33
 **/
public interface SellerToBuyerAppealService {
    int addSellerToBuyerAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    SellerToBuyerAppealBO queryAppeal(int appealId);
    int deleteAppeal(int appealId, int myId);
    SellerToBuyerAppealResultBO queryResult(int appealId);
    int updateAppeal(Map map);
    int queryMyId(int appealId);
}
