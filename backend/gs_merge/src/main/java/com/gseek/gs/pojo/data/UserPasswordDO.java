package com.gseek.gs.pojo.data;

import com.gseek.gs.pojo.dto.RegisterDTO;
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
    private Integer userId;
    private String userName;
    private String password;
    private String salt;

    public UserPasswordDO(Integer userId) {
        this.userId = userId;
    }

    public UserPasswordDO(RegisterDTO dto){
        userName=dto.getUserName();
        password=dto.getPassword();
    }

}
