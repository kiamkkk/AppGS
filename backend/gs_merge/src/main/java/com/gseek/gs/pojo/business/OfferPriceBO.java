package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/11-16:48
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferPriceBO implements BOPostService{

    private String offerUserName;
    private BigDecimal price;
    private Long time;

}
