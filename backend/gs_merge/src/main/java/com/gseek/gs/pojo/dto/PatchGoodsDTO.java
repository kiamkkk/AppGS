package com.gseek.gs.pojo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/9-22:31
 */
@Getter
@Setter
@NoArgsConstructor
public class PatchGoodsDTO extends PostGoodsDTO{
    private Integer goodId;
}
