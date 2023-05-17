package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/11-22:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOfferPriceDTO {
    private BigDecimal offerPrice;
    private Integer goodId;
    private Integer offerUserId;
    private Integer time;
}
