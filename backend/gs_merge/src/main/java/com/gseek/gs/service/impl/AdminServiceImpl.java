package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.dao.AdminMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.pojo.bean.AppealMessageBean;
import com.gseek.gs.pojo.bean.OrdinaryAdmin;
import com.gseek.gs.pojo.business.*;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.data.GoodCheckedDO;
import com.gseek.gs.service.inter.*;
import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.NoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    MessageController messageController;
    @Autowired
    @Lazy
    BuyerToSellerAppealService buyerToSellerAppealService;
    @Autowired
    SellerToBuyerAppealService sellerToBuyerAppealService;
    @Autowired
    MoneyService moneyService;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    BillService billService;
    @Autowired
    BlacklistService blacklistService;
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

        //todo adminDO查出来是null
        AdminDO adminDO =adminMapper.selectAdminByAdminName(adminName);
        if (adminDO ==null){
            log.info("AdminNameFound|"+adminName);
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
    public int auditGood(GoodCheckedDO goodCheckedDO) throws JsonProcessingException {
//        拿到goodId
        int goodId=goodCheckedDO.getGoodId();
        //通过审核通知卖家
        if(goodCheckedDO.isResult()){
            int toUserId=goodMapper.selectOwnUserIdByGoodId(goodId);
            NoticeMessage noticeMessage=new NoticeMessage("商品审核通过",System.currentTimeMillis(),toUserId);
            messageController.general(noticeMessage);
        }
        return adminMapper.auditGood(goodCheckedDO);
    }
    public int auditSellerAppeal(SellerToBuyerAppealResultBO sellerToBuyerAppealResultBO,int appealId,int adminId) throws JsonProcessingException {
        int billId=sellerToBuyerAppealService.queryAppeal(appealId).getBillId();
        int claimerId =sellerToBuyerAppealService.queryAppeal(appealId).getMyId();
        int respondentId=billService.selectBill(billId).getBuyerId();
        boolean result=sellerToBuyerAppealResultBO.isAppealResult();
        boolean accept=sellerToBuyerAppealResultBO.isAccept();
        int damageDegree=sellerToBuyerAppealResultBO.getDamageDegree();
//            审核通过&&卖家同意按协议走
        if (result&&accept){
//                    账号价值受损严重
            if(damageDegree==3){
//                    退钱
                moneyService.returnMoney(billId,respondentId);
//                    加入黑名单
                blacklistService.insertAuditedBlacklistBySeller(appealId,respondentId,adminId);
//                    消息通知
                AppealMessageBean appealMessageBean=buyerToSellerAppealService.message(appealId);
                messageController.appeal(appealMessageBean);
            }
            else {
//                等级为1或2的退款
                moneyService.returnMoneyByDegree(billId,sellerToBuyerAppealResultBO.getDamageDegree(),respondentId);
            }
        }
        // 账号没有损伤，审核不通过
        else if(!result){
            NoticeMessage noticeMessage=new NoticeMessage("您的账号未损伤，申诉不通过，有疑问可以询问客服",System.currentTimeMillis(),claimerId);
            messageController.general(noticeMessage);
        }
//                卖家不同意按协议走&&审核通过
        else {
            NoticeMessage noticeMessage=new NoticeMessage("申诉通过，请与买家私下调解，有疑问可以询问客服",System.currentTimeMillis(),claimerId);
            messageController.general(noticeMessage);
//                    通知买家
            int goodId=billService.selectBill(billId).getGoodId();
            String goodName=goodMapper.selectGoodByGoodIdFully(goodId).getGoodName();
            String appealReason=sellerToBuyerAppealService.queryAppeal(appealId).getAppealReason();
            String message="您因"+appealReason+"被商品"+goodName+"的卖家投诉成功，因对方不接收按协议退款，请私下和解，有疑问可以询问客服";
            NoticeMessage noticeMessage1=new NoticeMessage(message,System.currentTimeMillis(),respondentId);
            messageController.general(noticeMessage1);
        }
//          设定申诉被审核
        adminMapper.setSellerCheck(sellerToBuyerAppealResultBO.getAppealId());
        return adminMapper.auditSellerAppeal(sellerToBuyerAppealResultBO);
    }
    public int auditBuyerAppeal(BuyerToSellerAppealResultBO buyerToSellerAppealResultBO,int appealId,int adminId) throws JsonProcessingException {
//        拿到billId
        int billId=buyerToSellerAppealService.queryAppeal(appealId).getBillId();
//                审核通过，扣钱
        if (buyerToSellerAppealResultBO.isAppealResult()){
//            拿到卖家ID
            int respondentId=billService.selectBill(billId).getSellerId();
            moneyService.returnMoney(billId,respondentId);
//            int goodId=billService.selectBill(billId).getGoodId();
            BigDecimal price=goodMapper.selectPriceByBillId(billId);
//                如果余额足够就解冻
            if(moneyMapper.selectMoneyBOByUserId(respondentId).getRemain().compareTo(price)>0){
                moneyMapper.unfrozenUser(respondentId);
            }
//            加入黑名单
            blacklistService.insertAuditedBlacklistByBuyer(appealId,respondentId,adminId);
//                通知用户
            AppealMessageBean appealMessageBean=buyerToSellerAppealService.message(appealId);
            messageController.appeal(appealMessageBean);
        }
//          设定申诉被审核
        adminMapper.setBuyerCheck(appealId);
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
    public int selectRandomAdmin(){
        return adminMapper.selectRandomAdmin();
    }


}
