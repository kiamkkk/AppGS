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
    private boolean appealResult;
    private boolean checked;
    private int adminId;
    private int blackId;
    private String disapproveReason;
}
