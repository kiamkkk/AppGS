package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.dao.AdminMapper;
import com.gseek.gs.dao.BlacklistMapper;
import com.gseek.gs.pojo.bean.OrdinaryAdmin;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.pojo.data.UserPasswordDO;
import com.gseek.gs.service.inter.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Isabella
 * @create: 2023-05-17 20:16
 **/
@Slf4j
@Service("adminServiceImpl")
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    private final String AUTHORITIE_ORDINARYUSER="ADMIN";
    public void setAdminMapper(AdminMapper adminMapper){
        this.adminMapper=adminMapper;
    }
    public AdminDO selectAdminByAdminName(String adminName){
        return adminMapper.selectAdminByAdminName(adminName);
    }
    public UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AUTHORITIE_ORDINARYUSER));
        OrdinaryAdmin admin=new OrdinaryAdmin(adminName,authorities);

        AdminDO adminDO =adminMapper.selectAdminByAdminName(adminName);
        if (adminDO ==null){
            log.debug("AdminNameFound|"+adminName);
            throw new UsernameNotFoundException("AdminNameNotFound:"+adminName);
        }
        admin.setPassword(adminDO.getAdminPassword());
        admin.setAdminId(adminDO.getAdminId());
        return admin;
    }
    public List<GoodBO> queryUnCheckedProduct(){
        return adminMapper.queryUnCheckedProduct();
    }
    public List<SellerToBuyerAppealBO> queryUnCheckedSellerAppeal(){
        return adminMapper.queryUnCheckedSellerAppeal();
    }
    public List<BuyerToSellerAppealBO> queryUnCheckedBuyerAppeal(){
        return adminMapper.queryUnCheckedBuyerAppeal();
    }
    public SellerToBuyerAppealBO querySellerAppealById(int appealId){
        return adminMapper.querySellerAppealById(appealId);
    }
    public BuyerToSellerAppealBO queryBuyerAppealById(int appealId){
        return adminMapper.queryBuyerAppealById(appealId);
    }
    public int auditGood(GoodCheckedDO goodChecked){
        adminMapper.setGoodCheck(goodChecked.getGoodId());
        return adminMapper.auditGood(goodChecked);
    }
    public int auditSellerAppeal(SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO){
        adminMapper.setSellerCheck(sellerToBuyerAppealResultBO.getAppeal_id());
        return adminMapper.auditSellerAppeal(sellerToBuyerAppealResultBO);
    }
    public int auditBuyerAppeal(BuyerToSellerAppealResultBO buyerToSellerAppealResultBO){
        adminMapper.setBuyerCheck(buyerToSellerAppealResultBO.getAppeal_id());
        return adminMapper.auditBuyerAppeal(buyerToSellerAppealResultBO);
    }
    public int setGoodCheck(int goodId){
        return adminMapper.setGoodCheck(goodId);
    }
    public int setSellerCheck(int appealId){
        return adminMapper.setSellerCheck(appealId);
    }
    public int setBuyerCheck(int appealId){
        return adminMapper.setBuyerCheck(appealId);
    }
}
