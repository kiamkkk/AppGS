package com.gseek.gs.pojo.data;

import com.gseek.gs.common.RechargeWithdraw;
import com.gseek.gs.pojo.dto.RechargeDTO;
import com.gseek.gs.pojo.dto.WithdrawalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 充值提现记录.
 *
 * @author Phak
 * @since 2023/5/15-13:23
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RechargeWithdrawDO {

    private String rwId;
    private Integer userId;
    private Long createTime;
    private BigDecimal amount;
    private String type;

    public RechargeWithdrawDO(String rwId,int userId,RechargeDTO dto) {
        this.rwId=rwId;
        this.userId=userId;
        this.createTime= dto.getTime();;
        this.amount=dto.getChargeAmount();
        this.type= RechargeWithdraw.RECHARGE.name();
    }

    public RechargeWithdrawDO(String rwId, int userId, WithdrawalDTO dto) {
        this.rwId=rwId;
        this.userId=userId;
        this.createTime= dto.getTime();
        this.amount=dto.getWithdrawalAmount();
        this.type= RechargeWithdraw.WITHDRAW.name();
    }
}
