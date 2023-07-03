package com.gseek.gs.service.inter;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;

/**
 * @author: Isabella
 * @create: 2023-05-12 21:33
 **/
public interface BuyerToSellerAppealService {
    /**
     *添加买家申诉
     * */
    int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO,int billId) throws JsonProcessingException;
    /**
     *查看申诉
     * */
    BuyerToSellerAppealBO queryAppeal(int appealId);
    /**
     *删除申诉
     * */
    int deleteAppeal(int appealId) throws JsonProcessingException;
    /**
     *查看审核结果
     * */
    BuyerToSellerAppealResultBO queryResult(int appealId);
    /**
     *更新申诉内容
     * */
    int updateAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO);
    /**
     *查看申诉人ID
     * */
    int queryMyId(int appealId);
    /**
     *被申诉信息通知
     * */
    AppealMessageBean message(int appealId);
}
