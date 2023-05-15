package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.RechargeWithdrawDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/15-13:22
 */
@Mapper
public interface RechargeWithdrawMapper {
//todo 补充注释

    /**
     *
     * */
    int insertRecode(@Param("rw_do") RechargeWithdrawDO rechargeWithdrawDO);


}
