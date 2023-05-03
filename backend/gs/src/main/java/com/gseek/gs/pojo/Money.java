package com.gseek.gs.pojo;

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
public class Money {

    private Integer userId;
    private BigDecimal money;
    private Boolean frozen;

    public Money(Integer userId) {
        this.userId = userId;
    }
}
