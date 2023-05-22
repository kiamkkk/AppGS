package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.common.BillState;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.TimeoutConfig;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.BoughtGoodMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.UserPasswordMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.trade.BillStateNotAllowException;
import com.gseek.gs.exce.business.trade.GoodSoldException;
import com.gseek.gs.exce.business.trade.NoNeedToPayException;
import com.gseek.gs.pojo.business.BillStateBO;
import com.gseek.gs.pojo.business.GoodAccountBO;
import com.gseek.gs.pojo.data.BillDO;
import com.gseek.gs.pojo.data.GoodDO;
import com.gseek.gs.pojo.dto.*;
import com.gseek.gs.service.inter.BillService;
import com.gseek.gs.service.inter.MoneyService;
import com.gseek.gs.util.PasswordUtil;
import com.gseek.gs.util.StrUtil;
import com.gseek.gs.util.TimeoutUtil;
import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.NoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

/**
 * @author Phak
 * @since 2023/5/12-20:41
 */
@Service("billServiceImpl")
@Slf4j
public class BillServiceImpl implements BillService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageController messageController;
    @Autowired
    BillMapper billMapper;
    @Autowired
    UserPasswordMapper userPasswordMapper;
    @Autowired
    TimeoutUtil timeoutUtil;
    @Autowired
    @Qualifier("moneyServiceImpl")
    MoneyService moneyService;
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    Result result;
    @Autowired
    BoughtGoodMapper boughtGoodMapper;

    private static final String ROLE_SELLER="seller";
    private static final String ROLE_BUYER="buyer";

    @RabbitListener(queues = TimeoutConfig.QUEUE_TIMEOUT)
    public void receiveTimeout(Message message) {

        int billId = StrUtil.bytesToInt(message.getBody());
        log.info("订单过期，订单号为:\n{}",billId);
        // 改变过期订单状态
        BillDO billDO=new BillDO();
        billDO.setBillId(billId);
        billDO.setState(BillState.PAYt_TIMEOUT.name());
        billDO.setStateModifiedTime(System.currentTimeMillis());
        billMapper.updateBillState(billDO);
        log.info("订单号{}过期",billId);

        //通知双方
        BillDO billDO1=billMapper.selectBillByBillId(billId);
        NoticeMessage messageToSeller=new NoticeMessage("订单号"+billId+"过期，交易取消",
                System.currentTimeMillis() , billDO1.getSellerId());
        NoticeMessage messageToBuyer=new NoticeMessage("订单号"+billId+"过期，交易取消",
                System.currentTimeMillis() , billDO1.getBuyerId());

        messageController.general(messageToSeller);
        messageController.general(messageToBuyer);

    }

    @Override
    public String postBill(PostBillsDTO dto)
            throws IllegalBlockSizeException, BadPaddingException {
        BillDO billDO=new BillDO(dto);
        try{
            billMapper.insertBill(billDO);
            if (billDO.getBillId()==null || billDO.getBillId()==0) {
                log.error("主键不回显，检查BillMapper#insertBill对应的sql");
                throw new ServerException();
            }
            //启动订单待支付倒计时
            timeoutUtil.offerBill(billDO.getBillId()+"");

            ObjectNode objectNode=objectMapper.createObjectNode();
            objectNode.put("billId", PasswordUtil.encrypt(billDO.getBillId()+""));
            return objectNode.toPrettyString();

        } catch (Exception e) {
            if(e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                // 商品已经被卖出
                log.info("对一个商品建立多个订单|goodId={},billId={}",billDO.getGoodId(),billDO.getBillId());
                throw new GoodSoldException();
            }else {
                throw e;
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String payBill(PayBillDTO dto,int userId)
            throws JsonProcessingException, ForbiddenException {
        int billId= dto.getIntBillId();
        // 非该订单买家，则拒绝操作
        BillDO billDO = userHasRight(billId, userId, ROLE_BUYER);
        // 订单不处于待支付状态，则拒绝操作
        billHasState(billDO.getState(), BillState.PENDING_PAY, billId);

        // 取消订单待支付倒计时
        timeoutUtil.popBill(dto.getBillId());
        // 修改bill、good的状态
        BillDO patchBillDO=new BillDO(dto);
        billMapper.updateBillState(patchBillDO);
        Integer goodId = billMapper.selectGoodIdByBillId(patchBillDO.getBillId());
        goodMapper.updateGoodSelect(new GoodDO(goodId,true));
        // 添加已购买记录
        boughtGoodMapper.insertBoughtGoods(goodId, dto.getTime(), dto.getBuyerId());

        // 修改买家余额
        moneyService.payBill(dto.getBuyerId(), dto.getIntBillId());
        // 通知双方
        NoticeMessage messageToSeller=new NoticeMessage("订单号"+billId+"已支付，请及时交货",
                System.currentTimeMillis() , billDO.getSellerId());
        NoticeMessage messageToBuyer=new NoticeMessage("订单号"+billId+"已支付",
                System.currentTimeMillis() , billDO.getBuyerId());

        messageController.general(messageToSeller);
        messageController.general(messageToBuyer);

        return result.gainPutSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deliveryBill(PatchDeliveryBillDTO dto, int userId)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        int billId=dto.getIntBillId();
        // 非该订单卖家，则拒绝操作
        BillDO billDO=userHasRight(billId, userId, ROLE_SELLER);
        // 订单不处于待交货状态，则拒绝操作
        billHasState(billDO.getState(), BillState.PENDING_DELIVER, dto.getIntBillId());
        //修改订单状态
        BillDO patchBillDO=new BillDO(dto);
        billMapper.updateBillState(patchBillDO);

        if (dto.getDelivered()){
            // 确认交货,商品信息推送给买方
            GoodAccountBO bo =goodMapper.selectGoodAccountByBillId(Integer.parseInt(dto.getBillId()));
            bo.postService();
            messageController.delivery(dto.getTime(),bo);
        }else {
            // 不交货,取消交易
            // 返还交易金
            moneyService.returnMoney(dto.getIntBillId(), billDO.getSellerId());
            // 取消信息推送给买方
            NoticeMessage messageToBuyer=new NoticeMessage("订单号"+dto.getBillId()+"被卖家取消，余额已退回账户",
                    System.currentTimeMillis() , billDO.getBuyerId());
            messageController.general(messageToBuyer);
        }
         return result.gainPatchSuccess();
    }

    @Override
    public String getBillState(String billId)
            throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {

        BillStateBO bo=billMapper.selectBillStateBOByBillId(Integer.parseInt(billId));
        bo.postService();
        return objectMapper.writeValueAsString(bo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String patchBillCancel(PatchBillCancelDTO dto, int userId) throws JsonProcessingException {
        // 非该订单买家或卖家，则拒绝操作
        BillDO billDO=userHasRight(dto.getIntBillId(), userId, ROLE_BUYER,ROLE_SELLER);
        // 获取订单状态
        BillState state=BillState.gainEnumByState(billDO.getState());

        //卖家选择取消交易
        if (dto.getCancel()){
            if (BillState.TRADE_END==state || BillState.TRADE_CANCEL==state || BillState.TRADE_ANOMALY==state) {
                //交易已经结束
                throw new BillStateNotAllowException(state);
            } else {
                    //买家没支付，直接取消交易
                    //修改订单状态
                    billMapper.updateBillState(new BillDO(dto));
                    // 取消订单待支付倒计时
                    try {
                        timeoutUtil.popBill(dto.getBillId());
                    } catch (NoNeedToPayException e){
                        //订单已经支付，将钱还给买家
                        moneyService.returnMoney(dto.getIntBillId(), billDO.getSellerId());
                        // 商品改为未售出
                        int goodId= billMapper.selectGoodIdByBillId(dto.getIntBillId());
                        goodMapper.updateGoodSelect(new GoodDO(goodId,false));
                        // 删除购买记录
                        boughtGoodMapper.deleteBoughtGoods(goodId);
                    }
                }
            } else {
                //卖家选择不取消交易
                if (BillState.WRONG_INSPECT==state){
                   //买家否定验货结果
                    // todo 卖家怎么提交账户问题？？
                    //todo 重定向吗??
                }
            }
        return result.gainPatchSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String patchBillInspect(PatchBillInspectDTO dto, int userId) throws JsonProcessingException {
        // 非该订单买家，则拒绝操作
        BillDO billDO = userHasRight(dto.getIntBillId(), userId, ROLE_BUYER);
        // 订单不处于待验货状态，则拒绝操作
        billHasState(billDO.getState(), BillState.PENDING_INSPECT, dto.getIntBillId());
        if (dto.getInspected()){
            // 验货通过
            // 修改bill状态
            billMapper.updateBillState(new BillDO(dto));
            // 支付卖家
            moneyService.payToSeller(dto.getIntBillId(), billDO.getBuyerId());
        }else {
            // 验货不通过,通知卖家
            NoticeMessage noticeMessage=new NoticeMessage("订单号"+dto.getBillId()+"买家验货结果不通过，请检查商品情况",
                    System.currentTimeMillis(),billDO.getSellerId());
            messageController.general(noticeMessage);
        }
        return result.gainPatchSuccess();
    }

    /**
     * 确定用户是否有权操作订单.
     *
     * @param billId 订单号
     * @param userId 当前用户
     * @param roles 有权操作的用户角色,为买家、卖家,使用ROLE_BUYER、ROLE_SELLER
     *
     * @return billDO 订单信息,避免进一步操作时重复获取
     *
     * @throws ForbiddenException 无权操作
     * */
    private BillDO userHasRight(int billId, int userId, String... roles)
            throws ForbiddenException{
        BillDO billDO=billMapper.selectBillByBillId(billId);
        for (String role:roles){
            if (Objects.equals(role, ROLE_SELLER) && userId == billDO.getSellerId()){
                return billDO;
            }
            if (Objects.equals(role, ROLE_BUYER) && userId == billDO.getBuyerId()){
                return billDO;
            }
        }

        log.info("用户id{}非订单号{}的卖家或买家，拒绝操作",userId,billId);
        throw new ForbiddenException();
    }

    /**
     * 确定订单状态是否正确.
     * @param nowState 订单当前状态
     * @param rightState 操作该订单的正确状态
     * @param billId 订单id,用来打日志
     *
     * @throws BillStateNotAllowException 订单状态异常
     * */
    private void billHasState(String nowState, BillState rightState, int billId){
        BillState state=BillState.gainEnumByState(nowState);
        if (state != rightState){
            log.info("订单号{}状态为{}, 需要状态为{}, 拒绝操作",billId, nowState, rightState);
            throw new BillStateNotAllowException(state);
        }
    }
    @Override
    public BillDO selectBill(int billId){
        return billMapper.selectBillByBillId(billId);
    }
}
