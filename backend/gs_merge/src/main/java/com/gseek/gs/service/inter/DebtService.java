package com.gseek.gs.service.inter;

import com.gseek.gs.pojo.data.DebtDO;

import java.math.BigDecimal;

/**
 * @author: Isabella
 * @create: 2023-05-20 22:07
 **/
public interface DebtService {
    BigDecimal queryDebt(int respondentId);
    int updateDebt(DebtDO debtDO);
    int addDebt(DebtDO debtDO);
}
