package com.gseek.gs.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 一般用户,用于登录.
 *
 * @author Phak
 * @since 2023/5/3-13:23
 */
@NoArgsConstructor
@AllArgsConstructor
public class OrdinaryUser implements UserDetails {
    private String userName;
    private int userId;
    private String password;
    List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();

    public OrdinaryUser(String userName, List<GrantedAuthority> authorities) {
        this.userName = userName;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * 下面的都用不到
     * */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
