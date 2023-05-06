package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * user_information实体类
 *
 * @author Phak
 * @since 2023/5/2-19:54
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationDO {
    private Integer userId;
    private String email;
    private String profilePhoto;
}
