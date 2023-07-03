package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 16:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistResultBO {
    private boolean appealResult;
    private boolean checked;
    private int adminId;
    private String disapproveReason;
}
