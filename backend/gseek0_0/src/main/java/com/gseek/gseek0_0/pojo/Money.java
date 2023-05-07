package com.gseek.gseek0_0.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Money {
    private int userId;
    private double money;
    private boolean frozen;
}
