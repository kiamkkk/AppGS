package com.gseek.gs.service.inter;


import com.gseek.gs.pojo.bean.AppealMessageBean;
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
    int deleteAppeal(int appealId);
    SellerToBuyerAppealResultBO queryResult(int appealId);
    int updateAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    int queryMyId(int appealId);
    AppealMessageBean message(int appealId);
}
