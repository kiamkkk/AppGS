package com.gseek.gs.dao;

import com.gseek.gs.pojo.Money;
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
     * @param money 包含: userId 用户id
     *                   money 余额
     *                   frozen 钱包是否冻结
     *
     * @return row 受影响行数
     * */
    int insertMoney(@Param("money") Money money);
}
