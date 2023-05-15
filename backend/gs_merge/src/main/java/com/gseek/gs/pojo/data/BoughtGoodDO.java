package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/12-11:23
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoughtGoodDO {

    private Integer goodId;
    private Integer buyerId;
    private Long boughtTime;
    private BigDecimal boughtPrice;

}
