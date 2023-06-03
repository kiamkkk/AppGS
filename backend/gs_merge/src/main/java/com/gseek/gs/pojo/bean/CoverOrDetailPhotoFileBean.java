package com.gseek.gs.pojo.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Phak
 * @since 2023/6/1-21:24
 */
@Getter
@Setter
@AllArgsConstructor
public class CoverOrDetailPhotoFileBean {

    private Integer goodId;
    private List<MultipartFile> photos;

    public void setPhotos(List<MultipartFile> photos) {
        if (photos == null){
            this.photos=new ArrayList<>();
        }
        this.photos = photos;
    }
}
