package com.gseek.gs.util;

import com.gseek.gs.common.TokenGrade;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;


/**
 * 在token里新增gra字段，表示token所有者的权限.
 *
 * @author Phak
 * @since 2023/5/22-22:12
 * @deprecated 原本想通过继承DefaultClaims来在TokenUtil#extractClaim中
 * 使用CustomClaims::getGrade获取tokenGrade，但没办法实现.建议把TOKEN_GRADE挪到TokenUtil里
 */
public class CustomClaims extends DefaultClaims  {

    public static final String TOKEN_GRADE="gra";

    public String getGrade() {
        return getString(TOKEN_GRADE);
    }

    public Claims setGrade(TokenGrade gra) {
        setValue(TOKEN_GRADE, gra.name());
        return this;
    }


}
