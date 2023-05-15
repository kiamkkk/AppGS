package com.gseek.gs.websocket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 封装系统推送消息.
 *
 * @author Phak
 * @since 2023/5/8-22:46
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralMessage {
    /**
    * 发布时间
    * */
    private long time;
    /**
     * 标题
     * */
    private String topic;
    /**
     * 正文
     * */
    private String text;
    /**
     * 图片url数组
     * */
    private List<String> urls;

}
