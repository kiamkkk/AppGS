package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作recharge_withdraw表.
 * 这个表记录平台的支付宝账户资金流入、流出情况.
 *
 * @author Phak
 * @since 2023/5/15-13:22
 */
@Mapper
public interface RechargeWithdrawMapper {

    /**
     * 插入充值/提现记录.
     * */
    int insertRecode(@Param("rw_do") RechargeWithdrawDO rechargeWithdrawDO);

}
