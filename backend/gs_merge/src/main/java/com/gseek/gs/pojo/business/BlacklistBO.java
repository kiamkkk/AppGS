package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 16:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistBO {
    private int claimer_id;
    private int respondent_id;
    private String appeal_reason;
    private boolean checked;
    private String provePic;
    private int black_id;
}
