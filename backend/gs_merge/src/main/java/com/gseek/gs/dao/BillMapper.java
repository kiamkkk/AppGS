package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.BillStateBO;
import com.gseek.gs.pojo.data.BillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/9-13:01
 */
@Mapper
public interface BillMapper {
    //todo 补充注释

    /**
     *
     * */
    int insertBill(@Param("billDO") BillDO billDO);

    /**
     *
     * */
    int updateBillState(@Param("billDO") BillDO billDO);

    /**
     *
     */
    BillStateBO selectBillStateBOByBillId(@Param("billId") int billId);

    /**
     *
     * */
    Integer selectGoodIdByBillId(@Param("billId") int billId);

}
