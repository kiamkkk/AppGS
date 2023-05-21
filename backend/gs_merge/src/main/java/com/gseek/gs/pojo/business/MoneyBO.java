package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/15-16:51
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyBO implements BOPostService{

    private BigDecimal remain;
    private Boolean frozen;


}
