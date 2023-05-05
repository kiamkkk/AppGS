package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.MoneyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Phak
 * @since 2023/5/2-20:25
 */
@Mapper
public interface MoneyMapper {

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


}
