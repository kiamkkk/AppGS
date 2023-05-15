package com.gseek.gs.pojo.data;

import com.gseek.gs.common.BillState;
import com.gseek.gs.pojo.dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/9-13:13
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillDO {

    private Integer billId;
    private Integer goodId;
    private Long createTime;
    private Integer sellerId;
    private Integer buyerId;
    private String state;
    private Long stateModifiedTime;

    public BillDO(PostBillsDTO dto) {
        this.goodId = dto.getGoodId();
        this.createTime = dto.getTime();
        this.stateModifiedTime=dto.getTime();
        this.sellerId = dto.getSellerId();
        this.buyerId = dto.getBuyerId();
    }

    public BillDO(PayBillDTO dto){
        this.billId= Integer.valueOf(dto.getBillId());
        this.stateModifiedTime=dto.getTime();
        this.state=BillState.PENDING_DELIVER.getState();
        this.buyerId=dto.getBuyerId();
    }

    public BillDO(PatchDeliveryBillDTO dto){
        this.billId= Integer.valueOf(dto.getBillId());
        this.stateModifiedTime=dto.getTime();
        if (dto.getDelivered()) {
            this.state = BillState.PENDING_DELIVER.getState();
        } else {
            this.state = BillState.TRADE_CANCEL.getState();
        }
    }

    public BillDO(PatchBillCancelDTO dto){
        this.billId= Integer.valueOf(dto.getBillId());
        this.sellerId=dto.getSellerId();
        this.stateModifiedTime=dto.getTime();
        if (dto.getCancel()){
            this.state=BillState.TRADE_CANCEL.getState();
        }
    }

    public BillDO(PatchBillInspectDTO dto) {
        this.billId= Integer.valueOf(dto.getBillId());
        this.sellerId=dto.getBuyerId();
        this.stateModifiedTime=dto.getTime();
        if (dto.getInspected()){
            this.state=BillState.WRONG_INSPECT.getState();
        }else {
            this.state=BillState.TRADE_END.getState();
        }
    }
}
