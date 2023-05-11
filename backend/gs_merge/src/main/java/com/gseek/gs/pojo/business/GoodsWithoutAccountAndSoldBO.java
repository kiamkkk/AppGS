package com.gseek.gs.pojo.business;

import com.gseek.gs.pojo.data.GoodDO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 对应GoodService#getGoodsByTag的查询结果
 *
 * @author Phak
 * @since 2023/5/9-18:32
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsWithoutAccountAndSoldBO {
    private Integer goodId;
    private String goodName;
    private String goodText;
    private String ownUserName;
    private Integer ownUserId;
    private BigDecimal goodPrice;
    private Long createTime;
    private Long modifiedTime;
    private String typeTagName;
    private List<String> coverPictures;
    private List<String> detailPictures;


    public GoodsWithoutAccountAndSoldBO(GoodDO goodDO, List<String> coverPictures, List<String> detailPictures){
        this.goodId=goodDO.getGoodId();
        this.goodName=goodDO.getGoodName();
        this.ownUserName=goodDO.getOwnUserName();
        this.ownUserId=goodDO.getOwnUserId();
        this.goodPrice=goodDO.getGoodPrice();
        this.createTime=goodDO.getCreateTime();
        this.modifiedTime=goodDO.getModifiedTime();
        this.coverPictures=coverPictures;
        this.detailPictures=detailPictures;
    }


}
