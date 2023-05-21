package com.gseek.gs.service.impl;

import com.gseek.gs.dao.DebtMapper;
import com.gseek.gs.pojo.data.DebtDO;
import com.gseek.gs.service.inter.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author: Isabella
 * @create: 2023-05-20 22:08
 **/
@Service
public class DebtServiceImpl implements DebtService {
    @Autowired
    DebtMapper debtMapper;

    public void setDebtMapper(DebtMapper debtMapper) {
        this.debtMapper = debtMapper;
    }

    public BigDecimal queryDebt(int respondentId){
        return debtMapper.queryDebt(respondentId);
    }
    public int updateDebt(DebtDO debtDO){
        BigDecimal oldMoney=debtMapper.queryDebt(debtDO.getRespondentId());
        BigDecimal newMoney=oldMoney.add(debtDO.getMoney());
        DebtDO debtDO1=new DebtDO(newMoney,debtDO.getRespondentId());
        return debtMapper.updateDebt(debtDO1);
    }
    public int addDebt(DebtDO debtDO){
        return debtMapper.addDebt(debtDO);
    }
}
