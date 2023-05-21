package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import com.gseek.gs.util.PasswordUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Phak
 * @since 2023/5/9-22:31
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatchGoodsDTO implements DTOPerService{
    private Integer goodId;
    private String goodName;
    private String account;
    private String accountPassword;
    private BigDecimal price;
    private String ownUserName;
    private String type;
    private List<String> tag;
    private Long time;
    private String goodText;
    private List<MultipartFile> coverPicture;
    private List<MultipartFile> detailPictures;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean =new ParameterWrongBean();

        if (goodId==null || goodId <= 0){
            bean.addParameters("goodId",goodId+"");
        }
        if (goodName.isBlank()){
            bean.addParameters("goodName",goodName);
        }
        if (account.isBlank()){
            bean.addParameters("account", account);
        }
        if (accountPassword.isBlank()){
            bean.addParameters("accountPassword", accountPassword);
        }
        if (price==null || price.compareTo(BigDecimal.valueOf(0.00)) <=0){
            bean.addParameters("price", price+"");
        }
        if (ownUserName.isBlank()){
            bean.addParameters("ownUserName",ownUserName);
        }
        if (type.isBlank()){
            bean.addParameters("type", type);
        }
        if (time == null || time <= 0){
            bean.addParameters("time", time+"");
        }
        if (goodText.isBlank()){
            bean.addParameters("goodText", goodText);
        }

        if (! bean.getWrongParameters().isEmpty() ){
            throw new ParameterWrongException(bean);
        }

    }

    @Override
    public void autoDecrypt() throws IllegalBlockSizeException, BadPaddingException, JsonProcessingException {
        this.accountPassword= PasswordUtil.decrypt(accountPassword);
        this.account=PasswordUtil.decrypt(account);
    }

}
