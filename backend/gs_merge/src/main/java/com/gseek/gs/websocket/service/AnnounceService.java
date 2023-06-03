package com.gseek.gs.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.AnnounceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定期读取指定路径下的json文件作为公告内容.
 *
 * @author Phak
 * @since 2023/5/27-15:08
 */
@Service
public class AnnounceService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageController messageController;

    /**
     * 公告文件名：公告内容
     * */
    public static Map<String, AnnounceMessage> announces=new HashMap<>();

    public static final String ANNOUNCE_PATH ="/files/announces/";

    @Autowired
    MinioUtil minioUtil;

    /**
     * 每30分钟读取一次所有公告名
     * */
    @Async("async")
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void readAnnounce() throws IOException {
        List<String> names = minioUtil.listAnnounce();
        for (String name : names){
            // 本地有没有储存这个公告
            if ( ! announces.containsKey(name) ){
                // 没有发布过的公告，储存到本地并发布
                // 从minio取出的文件名自带路径
                InputStream inputStream=  minioUtil.getFile("", name);
                AnnounceMessage announceMessage =objectMapper.readValue(inputStream, AnnounceMessage.class);
                messageController.announce(announceMessage);
                announces.put(name, announceMessage);
            }
        }
    }


}
