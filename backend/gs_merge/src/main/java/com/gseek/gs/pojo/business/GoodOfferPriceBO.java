package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/11-22:46
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodOfferPriceBO {

    private Integer goodId;
    private String goodName;
    private String goodText;
    private String ownUserName;
    private String type;
    private BigDecimal goodPrice;
    private BigDecimal offerPrice;
    private Long time;
    private List<String> tag;
    private List<String> coverPictures;
    private List<String> detailPictures;


}
