package com.gseek.gs.pojo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/6/4-16:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodDetailBO {

    GoodsWithoutAccountAndSoldBO goodDetail;
    String ownerHeadSculpture;
    Boolean fav;

}
