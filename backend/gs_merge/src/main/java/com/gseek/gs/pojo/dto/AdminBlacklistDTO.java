package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 14:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBlacklistDTO {
    private boolean appeal_result;
    private boolean checked;
    private int admin_id;
    private int black_id;
    private String disapprove_reason;
}
