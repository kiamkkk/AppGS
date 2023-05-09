package com.gseek.gs.pojo.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 封装封面与详细图片文件数组
 *
 * @author Phak
 * @since 2023/5/9-23:21
 */
@Setter
@Getter
public class GoodPhotoFileBean {

    int goodId;
    List<MultipartFile> covers;
    List<MultipartFile> details;

    public GoodPhotoFileBean(int goodId, List<MultipartFile> covers, List<MultipartFile> details) {
        this.goodId = goodId;
        this.covers = covers;
        this.details = details;
    }
}
