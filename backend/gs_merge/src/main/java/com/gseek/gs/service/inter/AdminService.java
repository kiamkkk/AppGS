package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Isabella
 * @create: 2023-05-17 20:13
 **/
public interface AdminService extends UserDetailsService {
    AdminDO selectAdminByAdminName(String adminName);
    /**
     *管理员登录用
    * */

    @Override
    UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException;
    /**
     *查看所有未审核的商品
     * */
    List<GoodBO> queryUnCheckedProduct();
    /**
     *查看所有未审核的卖家申诉
     * */
    List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal();
    /**
     *查看所有未审核的买家申诉
     * */
    List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal();
    /**
     *通过appealId查询卖家申诉
     * */
    SellerToBuyerAppealBO querySellerAppealById(int appealId);
    /**
     *通过appealId查询买家申诉
     * */
    BuyerToSellerAppealBO queryBuyerAppealById(int appealId);
    /**
     *审核商品
     * */
    int auditGood(GoodCheckedDO goodChecked);
    /**
     *审核卖家申诉
     * */
    int auditSellerAppeal(SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO,int appealId,int adminId);
    /**
     *审核买家申诉
     * */
    int auditBuyerAppeal(BuyerToSellerAppealResultBO buyerToSellerAppealResultBO,int appealId,int adminId) throws JsonProcessingException;
    /**
     *以下三个都是设置被审核
     * */
    int setGoodCheck(int goodId);
    int setSellerCheck(int appealId);
    int setBuyerCheck(int appealId);
    /**
     *随机选取一个管理员
     * */
    int selectRandomAdmin();
}
