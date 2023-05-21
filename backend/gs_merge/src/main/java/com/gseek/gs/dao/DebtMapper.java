package com.gseek.gs.dao;

import com.gseek.gs.pojo.data.DebtDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * @author: Isabella
 * @create: 2023-05-20 22:03
 **/
@Mapper
public interface DebtMapper {
    BigDecimal queryDebt(int respondentId);
    int updateDebt(DebtDO debtDO);
    int addDebt(DebtDO debtDO);
}
