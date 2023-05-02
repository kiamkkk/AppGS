package com.gseek.gs.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * user_identification实体类
 *
 * @author Phak
 * @since 2023/5/2-20:06
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentification {
    private Integer userId;
    private String idNumber;
    private Boolean certified;
    private Long modifiedTime;

    public UserIdentification(Integer userId) {
        this.userId = userId;
    }
}
