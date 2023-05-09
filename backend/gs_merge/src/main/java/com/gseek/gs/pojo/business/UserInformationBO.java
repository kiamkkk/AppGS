package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 封装用户一般信息.
 *
 * @author Phak
 * @since 2023/5/5-23:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationBO {

    private Integer userId;
    private String email;
    private BigDecimal remain;
    private String headSculpture;

}
