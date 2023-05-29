package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.BuyerToSellerAppealBO;
import com.gseek.gs.pojo.business.BuyerToSellerAppealResultBO;
import com.gseek.gs.pojo.dto.BuyerToSellerAppealDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: Isabella
 * @create: 2023-05-12 15:01
 **/
@Mapper
public interface BuyerToSellerAppealMapper {
    //TODO 检测输入信息是否是有效信息（？
    /**
     *新增申诉
     * */
    int addBuyerToSellerAppeal(@Param("buyerToSellerAppealDTO")BuyerToSellerAppealDTO buyerToSellerAppealDTO);
    /**
     *查看申诉
     * */
    BuyerToSellerAppealBO queryAppeal(@Param("appealId")int appealId);
    /**
     *删除申诉
     * */
    int deleteAppeal(@Param("appealId")int appealId);
    /**
     *查看结果
     * */
    BuyerToSellerAppealResultBO queryResult(@Param("appealId")int appealId);
    /**
     *更新申诉
     * */
    int updateAppeal(@Param("buyerToSellerAppealDTO")BuyerToSellerAppealDTO buyerToSellerAppealDTO);
    /**
     *查看申诉者ID
     * */
    int queryMyId(@Param("appealId")int appealId);


}
