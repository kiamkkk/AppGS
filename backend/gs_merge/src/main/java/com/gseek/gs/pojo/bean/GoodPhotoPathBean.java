package com.gseek.gs.pojo.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 封装商品的封面url数组和详细图片url数组.
 *
 * @author Phak
 * @since 2023/5/9-22:59
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodPhotoPathBean {

    Integer goodId;
    private List<String> coverPaths;
    private List<String> detailPaths;




}
