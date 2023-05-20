package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-13 12:45
 **/
@Mapper
public interface SellerToBuyerAppealMapper {
    //TODO 和其他表的交互
    //TODO 检测输入信息是否是有效信息（？
    int addSellerToBuyerAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    SellerToBuyerAppealBO queryAppeal(int appealId);
    int deleteAppeal(int appealId);
    SellerToBuyerAppealResultBO queryResult(int appealId);
    int updateAppeal(SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    int queryMyId(int appealId);

}
