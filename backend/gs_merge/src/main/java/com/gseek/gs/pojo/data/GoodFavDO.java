package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/9-13:12
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodFavDO {

    private Integer userId;

    private Integer goodId;

    private Long favTime;

}
