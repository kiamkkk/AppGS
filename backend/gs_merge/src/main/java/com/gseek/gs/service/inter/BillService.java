package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.trade.GoodSoldException;
import com.gseek.gs.pojo.data.BillDO;
import com.gseek.gs.pojo.dto.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Phak
 * @since 2023/5/12-20:40
 */
public interface BillService {
    //todo 补充注释

    /**
     *
     * */
    String postBill(PostBillsDTO dto) throws ServerException, GoodSoldException;

    /**
     *  买家交钱暂存平台，通知双方
     *
     * */
    String payBill(PayBillDTO dto, int userId) throws JsonProcessingException;

    /**
     * 商品信息通知买方
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    String deliveryBill(PatchDeliveryBillDTO dto, int userId) throws JsonProcessingException, ServerException;

    /**
     *
     * */
    String getBillState(String billId) throws ServerException, JsonProcessingException;

    /**
     *
     * */
    @Transactional(rollbackFor = Exception.class)
    String patchBillCancel(PatchBillCancelDTO dto, int userId) throws JsonProcessingException;

    @Transactional(rollbackFor = Exception.class)
    String patchBillInspect(PatchBillInspectDTO dto, int userId) throws JsonProcessingException;
    BillDO selectBill(int billId);

}
