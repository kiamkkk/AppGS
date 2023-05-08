package com.gseek.gseek0_0.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {
    private int userId;
    private String appealReason;
    private String appealResult;
    private boolean checked;
    private int adminId;
}
