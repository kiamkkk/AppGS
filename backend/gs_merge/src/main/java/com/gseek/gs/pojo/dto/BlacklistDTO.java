package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Isabella
 * @create: 2023-05-12 14:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistDTO {
    private int claimerId;
    private int respondentId;
    private String appealReason;
//    private int black_id;
    private String provePic;

}
