package com.gseek.gs.service.inter;


import com.fasterxml.jackson.core.JsonProcessingException;
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
    /**
     *增加卖家申诉
     * */
    int addSellerToBuyerAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    /**
     *查看卖家申诉
     * */
    SellerToBuyerAppealBO queryAppeal(int appealId);
    /**
     *删除申诉
     * */
    int deleteAppeal(int appealId) throws JsonProcessingException;
    /**
     *查看申诉结果
     * */
    SellerToBuyerAppealResultBO queryResult(int appealId);
    /**
     *更新申诉内容
     * */
    int updateAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    /**
     *查看申诉人ID
     * */
    int queryMyId(int appealId);
    /**
     *被申诉信息通知
     * */
    AppealMessageBean message(int appealId);
}
