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
     * 主键回显。
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
}
