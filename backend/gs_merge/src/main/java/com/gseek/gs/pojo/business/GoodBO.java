package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/11-15:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodBO implements BOPostService{

    private Integer goodId;
    private String goodName;
    private String account;
    private String accountPassword;
    private String goodText;
    private String ownUserName;
    private Integer ownUserId;
    private BigDecimal goodPrice;
    private Long createTime;
    private Long modifiedTime;
    private Boolean sold;
    private String typeTagName;
    private List<String> tagNames;
    private List<String> coverPictures;
    private List<String> detailPictures;

}
