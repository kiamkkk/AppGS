package com.gseek.gs.pojo;

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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPassword {
    private Integer userId;
    private String userName;
    private String password;
    private String salt;
}
