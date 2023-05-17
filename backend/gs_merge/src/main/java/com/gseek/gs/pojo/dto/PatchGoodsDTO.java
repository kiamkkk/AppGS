package com.gseek.gs.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Phak
 * @since 2023/5/9-22:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchGoodsDTO extends PostGoodsDTO{
    private Integer goodId;
}
