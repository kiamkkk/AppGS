package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BillStateBO;
import com.gseek.gs.pojo.data.BillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作bill表.
 *
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface BillMapper {
    //todo 补充注释

    /**
     * 插入新行.
     * 主键回显.
     * */
    int insertBill(@Param("billDO") BillDO billDO);
    /**
     * 更新订单情况.
     * 更新值为state、stateModifiedTime.
     * */
    int updateBillState(@Param("billDO") BillDO billDO);
    /**
     * 通过订单id,获取商品信息与商品售价.
     */
    BillStateBO selectBillStateBOByBillId(@Param("billId") int billId);
    /**
     * 通过订单id,获取商品id.
     * */
    Integer selectGoodIdByBillId(@Param("billId") int billId);
    /**
     * 通过订单id,获取订单信息.
     * */
    BillDO selectBillByBillId(@Param("billId")int billId);
    /**
     * 通过商品id,获取正在交易的商品信息.
     * 希望根据这个方法来检查有无在交易中的商品.
     * 我觉得返回BillDO不好,能不能在dao层就进行判断?
     * 我又觉得这整个bill表设计的都不合理,但我也没法改了.
     * */
    //todo 取消goodId的唯一索引; 插入新bill时检查有无state为在交易的goodId
    BillDO selectBillByGoodId(@Param("goodId") int goodId);
}
