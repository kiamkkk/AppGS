package com.gseek.gs.config.login.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义AuthenticationToken,在这里存放userId之后取用
 *
 * @author Phak
 * @since 2023/5/3-13:32
 */
@Getter
@Setter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private int userId;

    public CustomAuthenticationToken(Object principal, Object credentials,int userId) {
        super(principal, credentials);
        this.userId=userId;
    }

    public CustomAuthenticationToken(Object principal, Object credentials,
                                     Collection<? extends GrantedAuthority> authorities,int userId) {
        super(principal, credentials, authorities);
        this.userId=userId;
    }
}
