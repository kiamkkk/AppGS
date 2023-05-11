package com.gseek.gs.pojo.bean;

/**
 * 封装登录信息.
 *
 * @author Phak
 * @since 2023/5/3-13:34
 */
public class AuthenticationBean {
    private String userName;
    private String password;

    public String getUserName() {
        return (userName != null) ? userName : "";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return (password != null) ? password : "";
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
