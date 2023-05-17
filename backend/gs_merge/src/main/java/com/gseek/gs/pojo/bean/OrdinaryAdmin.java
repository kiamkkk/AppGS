package com.gseek.gs.pojo.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Isabella
 * @create: 2023-05-17 16:04
 **/
public class OrdinaryAdmin implements UserDetails {
    private String adminName;
    private Integer adminId;
    private String password;
    List<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();

    public OrdinaryAdmin(String adminName, List<GrantedAuthority> authorities) {
        this.adminName = adminName;
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
        return adminName;
    }

    public int getAdminId() {
        return adminId;
    }
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    public void setAdminId(int userId) {
        this.adminId = userId;
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
