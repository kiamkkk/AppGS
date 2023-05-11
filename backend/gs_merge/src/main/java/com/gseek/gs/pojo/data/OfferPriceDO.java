package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/11-16:53
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferPriceDO {

    private Integer bidderId;
    private Integer sellerId;
    private BigDecimal offerPrice;
    private Integer goodId;
    private Long offerTime;

}
