package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: Isabella
 * @create: 2023-05-12 15:01
 **/
@Mapper
public interface BuyerToSellerAppealMapper {
    //TODO 检测输入信息是否是有效信息（？
    int addBuyerToSellerAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO);

    BuyerToSellerAppealBO queryAppeal(int appealId);

    int deleteAppeal(int appealId);
    BuyerToSellerAppealResultBO queryResult(int appealId);
    int updateAppeal(BuyerToSellerAppealDTO buyerToSellerAppealDTO);
    int queryMyId(int appealId);


}
