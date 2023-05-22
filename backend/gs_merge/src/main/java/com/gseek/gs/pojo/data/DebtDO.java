package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author: Isabella
 * @create: 2023-05-20 22:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtDO {
    private BigDecimal money;
    private int respondentId;
}
