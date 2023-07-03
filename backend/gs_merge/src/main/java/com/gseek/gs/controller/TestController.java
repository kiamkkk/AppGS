package com.gseek.gs.controller;

import com.gseek.gs.websocket.controller.MessageController;
import com.gseek.gs.websocket.message.AnnounceMessage;
import com.gseek.gs.websocket.message.NoticeMessage;
import com.gseek.gs.websocket.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试组件能否在web端运行.
 *
 * @author Phak
 * @since 2023/5/3-22:23
 * @deprecated 测试功能用postman或直接进行接口联调.
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageController messageController;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @ResponseBody
    @GetMapping("/anno")
    public void sendAnnounce(){
        messageService.sendMessage(new AnnounceMessage("test by sendAnnounce", 1145141919810L));
    }

    @ResponseBody
    @GetMapping("/notice/{user_id}")
    public String sendNotice(@PathVariable("user_id") String userId){
        try {
            int ui = Integer.parseInt(userId);
            if (ui == 0){
                return "{\"message\":\"user_id error\"}";
            }
            messageService.sendMessage(new NoticeMessage("test by sendNotice", 1145141919810L, ui));
        }catch (Exception e){
            return "{\"message\":\"user_id error\"}";
        }
        return "{\"message\":\"success\"}";
    }

}
