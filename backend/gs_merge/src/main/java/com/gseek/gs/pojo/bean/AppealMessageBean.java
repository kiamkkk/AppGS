package com.gseek.gs.pojo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-24 23:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppealMessageBean {
    private Integer toUserId;
    private String goodName;
    private Integer billId;

}
