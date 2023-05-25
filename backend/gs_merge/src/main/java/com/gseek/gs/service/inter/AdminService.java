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

    @Override
    UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException;
    List<GoodBO> queryUnCheckedProduct();
    List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal();
    List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal();
    SellerToBuyerAppealBO querySellerAppealById(int appealId);
    BuyerToSellerAppealBO queryBuyerAppealById(int appealId);
    int auditGood(GoodCheckedDO goodChecked);
    int auditSellerAppeal(SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO);
    int auditBuyerAppeal(BuyerToSellerAppealResultBO buyerToSellerAppealResultBO);
    int setGoodCheck(int goodId);
    int setSellerCheck(int appealId);
    int setBuyerCheck(int appealId);
    int selectRandomAdmin();
}
