package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/9-13:13
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodCoverPicDO {

    private Integer goodId;

    private String coverPictureUrl;
}
