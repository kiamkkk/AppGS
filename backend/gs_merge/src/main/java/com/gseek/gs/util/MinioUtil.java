package com.gseek.gs.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Phak
 * @since 2023/5/2-21:09
 */
@Component
public class MinioUtil {
    //todo 增、删、改、根据路径获取文件流。要有批量操作。

    public String saveProfilePhoto(int userId, MultipartFile profilePhoto){
        //todo 储存图片并返回路径
        return "";
    }
}
