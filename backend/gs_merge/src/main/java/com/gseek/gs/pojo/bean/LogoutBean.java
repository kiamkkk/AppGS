package com.gseek.gs.pojo.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/6/14-15:23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutBean{
    private String userName;
    private String password;

    public String getUserName() {
        return  (userName != null) ? userName : "";
    }

    public String getPassword() {
        return (password != null) ? password : "";
    }


}
