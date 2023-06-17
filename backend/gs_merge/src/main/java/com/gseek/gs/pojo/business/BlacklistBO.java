package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-06-15 20:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistBO {
    private String claimerName;
    private String respondentName;
    private String appealReason;
    private boolean checked;
    private String provePic;
    private int blackId;
}
