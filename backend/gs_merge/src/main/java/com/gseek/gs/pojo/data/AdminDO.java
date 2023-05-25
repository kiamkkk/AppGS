package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * admin实体类
 *
 * @author Isabella
 * @since 2023/5/2-19:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDO {

    private Integer adminId;
    private String adminPassword;
    private String adminName;
    private String salt;

}
