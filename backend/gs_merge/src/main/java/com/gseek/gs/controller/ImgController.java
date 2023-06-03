package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.common.Result;
import com.gseek.gs.exce.business.imgs.FileNotFoundException;
import com.gseek.gs.util.MinioUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;

/**
 * 处理图片获取.
 *
 * @author Phak
 * @since 2023/5/15-16:57
 */
@Controller
@Slf4j
@RequestMapping("/imgs")
public class ImgController {
// todo 能不能在一个响应中返回多个图片？？？
    @Autowired
    MinioUtil minioUtil;
    
    @Autowired
    Result result;

    /**
     * 获取头像.
     * */
    @GetMapping("/profile_photos/{pic_name}.jpg")
    public String getProfile(HttpServletResponse response,
                             @PathVariable("pic_name") String picName)
            throws FileNotFoundException, JsonProcessingException {
        String path=MinioUtil.PATH_HEAD_SCULPTURES;
        String filename = new String((picName+MinioUtil.SUFFIX_JPG).getBytes(), StandardCharsets.UTF_8);
        minioUtil.downloadImg(response, path, filename);
        return result.gainGetSuccess();
    }
    /**
     * 获取商品封面.
     * */
    @GetMapping("/goods/{good_id}/covers/{cover_id}.jpg")
    public String getGoodCovers(HttpServletResponse response,
                                @PathVariable("good_id") int goodId, @PathVariable("cover_id") int coverId)
            throws FileNotFoundException, JsonProcessingException {
        String path=MinioUtil.PATH_GOOD_PICTURE+goodId+"/covers/";
        String filename = new String((coverId+MinioUtil.SUFFIX_JPG).getBytes(), StandardCharsets.UTF_8);
        minioUtil.downloadImg(response, path, filename);
        return result.gainGetSuccess();
    }
    /**
     * 获取商品详情图片.
     * */
    @GetMapping("/goods/{good_id}/details/{detail_id}.jpg")
    public String getGoodDetails(HttpServletResponse response,
                                 @PathVariable("good_id") int goodId, @PathVariable("detail_id") int detailId)
            throws FileNotFoundException, JsonProcessingException {
        String path=MinioUtil.PATH_GOOD_PICTURE+goodId+"/details/";
        String filename = new String((detailId+MinioUtil.SUFFIX_JPG).getBytes(), StandardCharsets.UTF_8);
        minioUtil.downloadImg(response, path, filename);
        return result.gainGetSuccess();
    }
    /**
     * 获取聊天记录图片.
     * */
    @GetMapping("/imgs/chats/{good_id}/{chat_id}.jpg")
    public String getGoodCovers(HttpServletResponse response,
                                @PathVariable("good_id") int goodId, @PathVariable("chat_id") String chatId)
            throws FileNotFoundException, JsonProcessingException {
        String path=MinioUtil.PATH_CHATS+goodId+"/";
        String filename = new String((chatId+MinioUtil.SUFFIX_JPG).getBytes(), StandardCharsets.UTF_8);
        minioUtil.downloadImg(response, path, filename);
        return result.gainGetSuccess();
    }
}
