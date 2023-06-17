package com.gseek.gs.pojo.business;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/11-23:06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodFavBO implements BOPostService{

    private Integer goodId;
    private String goodName;
    private String goodText;
    private String ownUserName;
    private String type;
    private BigDecimal goodPrice;
    private Long favTime;
    private List<String> tag;
    private List<String> coverPictures;
    private List<String> detailPictures;

}
