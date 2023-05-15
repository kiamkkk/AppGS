package com.gseek.gs.dao;

import com.gseek.gs.pojo.business.MoneyBO;
import com.gseek.gs.pojo.data.MoneyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/2-20:25
 */
@Mapper
public interface MoneyMapper {

    //todo 补充注释

    /**
     * 插入新一般用户的余额信息.
     * 注册用户的第四个步骤.
     *
     * @param moneyDO 包含: userId 用户id
     *                   moneyDO 余额
     *                   frozen 钱包是否冻结
     *
     * @return row 受影响行数
     * */
    int insertMoney(@Param("moneyDO") MoneyDO moneyDO);

    /**
     *
     * */
    BigDecimal selectRemainByUserId(@Param("userId") int userId);

    /**
     *
     * */
    int minusRemainByUserId(@Param("userId") int userId,@Param("price") BigDecimal price);

    int addRemainByUserId(@Param("userId") int userId,@Param("price") BigDecimal price);

    int returnMoney(@Param("billId") int billId);

    int payToSeller(@Param("billId") int billId);

    MoneyBO selectMoneyBOByUserId(@Param("userId") int userId);

}
