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
    /**update by Isabella at 2023/5/17 21:59**/
    private String adminName;
    private String adminPassword;
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
    public String getAdminName() {
        return (adminName != null) ? adminName : "";
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return (adminPassword != null) ? adminPassword : "";
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
