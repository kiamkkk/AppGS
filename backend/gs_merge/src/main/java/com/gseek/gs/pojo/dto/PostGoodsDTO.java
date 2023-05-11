package com.gseek.gs.pojo.dto;

import com.gseek.gs.util.PasswordUtil;
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
 * @since 2023/5/9-22:24
 */
@Getter
@Setter
@NoArgsConstructor
public class PostGoodsDTO {

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


    public void setAccount(String account) throws IllegalBlockSizeException, BadPaddingException {
        this.account=PasswordUtil.decrypt(account);
    }


    public void setAccountPassword(String accountPassword) throws IllegalBlockSizeException, BadPaddingException {
        this.accountPassword=PasswordUtil.decrypt(accountPassword);
    }

}
