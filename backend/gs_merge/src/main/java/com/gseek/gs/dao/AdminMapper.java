package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Isabella
 * @create: 2023-05-17 16:50
 **/
@Mapper
public interface AdminMapper {
    //TODO 要不要能添加
    int insertAdmin(AdminDO adminDO);

    AdminDO selectAdminByAdminName( String adminName);

    int updateAdmin(AdminDO adminDO);
    String selectAdminNameByAdminId( int adminId);
    List<GoodBO> queryUnCheckedProduct();
    List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal();
    List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal();
    SellerToBuyerAppealBO querySellerAppealById(int appealId);
    BuyerToSellerAppealBO queryBuyerAppealById(int appealId);
    //TODO 缺少把商品加入未审核表的SQL
    int auditGood(GoodCheckedDO goodChecked);
    int auditSellerAppeal(SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO);
    int auditBuyerAppeal(BuyerToSellerAppealResultBO buyerToSellerAppealResultBO);
    int setGoodCheck(int goodId);
    int setSellerCheck(int appealId);
    int setBuyerCheck(int appealId);
}
