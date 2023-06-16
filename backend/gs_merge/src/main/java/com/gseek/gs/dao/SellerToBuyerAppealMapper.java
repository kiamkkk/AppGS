package com.gseek.gs.dao;


import com.gseek.gs.pojo.business.SellerToBuyerAppealBO;
import com.gseek.gs.pojo.business.SellerToBuyerAppealResultBO;
import com.gseek.gs.pojo.dto.SellerToBuyerAppealDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author: Isabella
 * @create: 2023-05-13 12:45
 **/
@Mapper
public interface SellerToBuyerAppealMapper {
    /**
     *增加申诉
     * */
    int addSellerToBuyerAppeal(@Param("sellerToBuyerAppealDTO")SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    /**
     *查看申诉
     * */
    SellerToBuyerAppealBO queryAppeal(@Param("appealId")int appealId);
    /**
     *删除申诉
     * */
    int deleteAppeal(@Param("appealId")int appealId);
    /**
     *查看申诉结果
     * */
    SellerToBuyerAppealResultBO queryResult(@Param("appealId")int appealId);
    /**
     *更新申诉
     * */
    int updateAppeal(@Param("sellerToBuyerAppealDTO")SellerToBuyerAppealDTO sellerToBuyerAppealDTO);
    /**
     *查看申诉人ID
     * */
    int queryMyId(@Param("appealId")int appealId);

}
