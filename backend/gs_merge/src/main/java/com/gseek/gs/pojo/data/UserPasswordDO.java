package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * user_password实体类.
 *
 * @author Phak
 * @since 2023/5/2-19:32
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordDO {

    public UserPasswordDO(Integer userId) {
        this.userId = userId;
    }

    private Integer userId;
    private String userName;
    private String password;
    private String salt;

}
