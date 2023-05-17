package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gseek.gs.common.BillState;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.TimeoutConfig;
import com.gseek.gs.dao.BillMapper;
import com.gseek.gs.dao.GoodMapper;
import com.gseek.gs.dao.UserPasswordMapper;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.ToBeConstructed;
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
import com.rabbitmq.client.impl.AMQImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

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

    @Override
    public String postBill(PostBillsDTO dto) {
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
                //todo 对一个商品建立多个订单，应该抛出一个业务异常
                log.info("对一个商品建立多个订单|goodId={},billId={}",billDO.getGoodId(),billDO.getBillId());
                throw new ToBeConstructed();
            }else {
                throw e;
            }
        }
    }

    @RabbitListener(queues = TimeoutConfig.QUEUE_TIMEOUT)
    public void receiveTimeout(Message message, AMQImpl.Channel channel)
            throws IOException {

        int billId = StrUtil.bytesToInt(message.getBody());
        log.info("订单过期，订单号为:\n{}",billId);
        //todo 处理过期订单.
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String payBill(PayBillDTO dto) throws JsonProcessingException {
        // 修改bill、good的状态
        BillDO billDO=new BillDO(dto);
        billMapper.updateBillState(billDO);
        int goodId= billMapper.selectGoodIdByBillId(billDO.getBillId());
        goodMapper.updateGoodSelect(new GoodDO(goodId,true));
        // 修改买家余额
        moneyService.payBill(dto.getBuyerId(), Integer.parseInt(dto.getBillId()));
        // 取消订单待支付倒计时
        timeoutUtil.popBill(dto.getBillId());
        //todo  通知双方


        return result.gainPutSuccess();
    }

    @Override
    public String deliveryBill(PatchDeliveryBillDTO dto) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        BillDO patchBillDO=new BillDO(dto);
        //修改订单状态
        billMapper.updateBillState(patchBillDO);

        if (dto.getDelivered()){
            // 确认交货,商品信息推送给买方
            GoodAccountBO bo =goodMapper.selectGoodAccountByBillId(Integer.parseInt(dto.getBillId()));
            bo.postService();
            messageController.delivery(dto.getTime(),bo);
        }else {
            // 不交货,取消交易,取消信息推送给买方
            //todo 取消信息推送给买方

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

    @Override
    public String patchBillCancel(PatchBillCancelDTO dto) throws JsonProcessingException {
        BillDO patchBillDO=new BillDO(dto);
        //获取订单状态
        BillState state=BillState.gainEnumByState(
                billMapper.selectBillStateBOByBillId(patchBillDO.getBillId()).getState()
        );

        //卖家选择取消交易
        if (dto.getCancel()){
            if (BillState.TRADE_END==state || BillState.TRADE_CANCEL==state || BillState.TRADE_ANOMALY==state) {
                //交易已经结束
                //todo 交易结束异常
                throw new ToBeConstructed();
            } else {
                    //买家没验货，直接取消交易
                    //修改订单状态
                    billMapper.updateBillState(patchBillDO);

                    // 取消订单待支付倒计时
                    try {
                        timeoutUtil.popBill(dto.getBillId());
                    } catch (ToBeConstructed toBeConstructed){
                        //订单已经支付，将钱还给买家
                        moneyService.returnMoney(Integer.parseInt(dto.getBillId()));
                        int goodId= billMapper.selectGoodIdByBillId(Integer.parseInt(dto.getBillId()));
                        goodMapper.updateGoodSelect(new GoodDO(goodId,false));
                    }
            }
            } else {
                //卖家选择不取消交易
                if (BillState.WRONG_INSPECT==state){
                   //买家否定验货结果
                    //todo 不同意：投诉
                }
        }
        return result.gainPatchSuccess();
    }

    @Override
    public String patchBillInspect(PatchBillInspectDTO dto) throws JsonProcessingException {
        BillDO patchBillDO=new BillDO(dto);
        if (dto.getInspected()){
            // 验货通过
            // 修改bill状态
            billMapper.updateBillState(patchBillDO);
            // 支付卖家
            moneyService.payToSeller(patchBillDO.getBillId());
        }else {
            // 验货不通过
            //todo 通知卖家
        }
        return result.gainPatchSuccess();
    }
}
