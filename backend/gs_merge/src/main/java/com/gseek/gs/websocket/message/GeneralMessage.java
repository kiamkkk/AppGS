package com.gseek.gs.websocket.message;

/**
 * 封装系统推送消息.
 *
 * @author Phak
 * @since 2023/5/8-22:46
 */
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
    private String[] urls;

    public GeneralMessage(long time, String topic, String text, String[] urls) {
        this.time = time;
        this.topic = topic;
        this.text = text;
        this.urls = urls;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }
}
