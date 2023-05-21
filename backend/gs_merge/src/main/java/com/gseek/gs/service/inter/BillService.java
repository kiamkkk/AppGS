package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.dto.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author Phak
 * @since 2023/5/12-20:40
 */
public interface BillService {
    //todo 补充注释

    /**
     *
     * */
    String postBill(PostBillsDTO dto);

    /**
     *  买家交钱暂存平台，通知双方
     *
     * */
    String payBill(PayBillDTO dto, int userId) throws JsonProcessingException;

    /**
     * 商品信息通知买方
     *
     * */
    String deliveryBill(PatchDeliveryBillDTO dto, int userId) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException;

    /**
     *
     * */
    String getBillState(String billId) throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException;

    /**
     *
     * */
    String patchBillCancel(PatchBillCancelDTO dto, int userId) throws JsonProcessingException;

    String patchBillInspect(PatchBillInspectDTO dto, int userId) throws JsonProcessingException;


}
