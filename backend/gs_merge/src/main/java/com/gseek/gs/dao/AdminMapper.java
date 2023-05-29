package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Isabella
 * @create: 2023-05-17 16:50
 **/
@Mapper
public interface AdminMapper {

    int insertAdmin(@Param("adminDO") AdminDO adminDO);
    /**
     *通过管理员姓名查看管理员
     * */
    AdminDO selectAdminByAdminName(@Param("adminName")String adminName);

    int updateAdmin(@Param("adminDO")AdminDO adminDO);
    String selectAdminNameByAdminId(@Param("adminId") int adminId);
    /**
     *查看未审核商品
     * */
    List<GoodBO> queryUnCheckedProduct();
    /**
     *查看未审核卖家申诉
     * */
    List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal();
    /**
     *查看未审核买家申诉
     * */
    List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal();
    /**
     *通过appealId查看卖家申诉
     * */
    SellerToBuyerAppealBO querySellerAppealById(@Param("appealId")int appealId);
    /**
     *通过appealId查看买家申诉
     * */
    BuyerToSellerAppealBO queryBuyerAppealById(@Param("appealId")int appealId);
    /**
     *审核商品
     * */
    int auditGood(@Param("goodChecked")GoodCheckedDO goodChecked);
    /**
     *审核卖家申诉
     * */
    int auditSellerAppeal(@Param("sellerToBuyerAppealResultBO")SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO);
    /**
     *买家商品
     * */
    int auditBuyerAppeal(@Param("buyerToSellerAppealResultBO")BuyerToSellerAppealResultBO buyerToSellerAppealResultBO);
    /**
     *以下三个都是将状态改为已审核
     * */
    int setGoodCheck(@Param("goodId")int goodId);
    int setSellerCheck(@Param("appealId")int appealId);
    int setBuyerCheck(@Param("appealId")int appealId);
    /**
     *随机选择的管理员
     * */
    int selectRandomAdmin();
}
