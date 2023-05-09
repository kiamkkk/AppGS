package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * money实体类
 *
 * @author Phak
 * @since 2023/5/2-20:21
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDO {

    private Integer userId;
    private BigDecimal remain;
    private Boolean frozen;

    public MoneyDO(Integer userId) {
        this.userId = userId;
    }
}
