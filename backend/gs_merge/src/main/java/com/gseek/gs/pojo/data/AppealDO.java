package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * appeal实体类
 *
 * @author Isabella
 * @since 2023/5/2-19:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppealDO {
    private int userId;
    private String appealReason;
    private String appealResult;
    private boolean checked;
    private int adminId;
}
