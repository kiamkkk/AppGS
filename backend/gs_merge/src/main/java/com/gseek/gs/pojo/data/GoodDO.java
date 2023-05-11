package com.gseek.gs.pojo.data;

import com.gseek.gs.pojo.dto.PatchGoodsDTO;
import com.gseek.gs.pojo.dto.PostGoodsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Good实体类
 *
 * @author Phak
 * @since 2023/5/9-13:09
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodDO {

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
    private Integer typeTagId;
    private String typeTagName;

    public GoodDO(Integer goodId) {
        this.goodId = goodId;
    }
    
    public GoodDO(int ownUserId, String ownUserName, PostGoodsDTO dto,TagDO type){

        this.goodName=dto.getGoodName();
        this.account=dto.getAccount();
        this.accountPassword= dto.getAccountPassword();
        this.goodText=dto.getGoodText();
        this.ownUserId=ownUserId;
        this.ownUserName=ownUserName;
        this.goodPrice= dto.getPrice();
        this.createTime=dto.getTime();
        this.modifiedTime=dto.getTime();
        this.typeTagId=type.getTagId();
        this.typeTagName=type.getTagText();
        this.sold=Boolean.FALSE;

    }

    public GoodDO(String ownUserName,PatchGoodsDTO dto){
        this.goodId= dto.getGoodId();
        this.goodName=dto.getGoodName();
        this.account=dto.getAccount();
        this.accountPassword= dto.getAccountPassword();
        this.goodText=dto.getGoodText();
        this.ownUserName=ownUserName;
        this.goodPrice= dto.getPrice();
        this.modifiedTime=System.currentTimeMillis();
    }
}
