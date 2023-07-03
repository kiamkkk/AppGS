package com.gseek.gs.exce.business.websocket.chat;

import com.gseek.gs.exce.business.BusinessException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Phak
 * @since 2023/7/2-13:59
 */
@Getter
@Slf4j
public class WebSocketException extends BusinessException {
    /**
     * 造成错误的原消息
     * */
    protected Message sourceMessage;
    /**
     * 错误消息应该发送给哪个用户id
     * */
    protected String toUserId;
    /**
     * 发送到哪个频道上
     * */
    protected String destination;
    protected String sessionId;


    public WebSocketException(String message, Message sourceMessage, String toUserId, String destination, String sessionId) {
        super(message);
        this.sourceMessage = sourceMessage;
        this.toUserId = toUserId;
        this.destination = destination;
        this.sessionId = sessionId;
    }

    /**
     * 返回错误信息
     * */
    public byte[] toMessagePayload(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{")
                .append("code:").append(code).append(",\n")
                .append("message:\"").append(msg).append("\",\n")
                .append("Source Message:").append("\"\n")
                .append( printMessage(sourceMessage) ).append("\"")
                .append("}");
        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     *
     * */
    private String printMessage(Message message){
        StompHeaderAccessor accessor = SimpMessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String command = "NULL";
        String subId = "null";
        String destination = "null";

        if (accessor != null) {
            StompCommand stompCommand = accessor.getCommand();
            if (stompCommand == null){
                log.warn("StompCommand为null");
            }else {
                command = stompCommand.toString();
            }

            List<String> subIds = accessor.getNativeHeader("id");
            if (subIds != null){
                subId = subIds.get(0);
            }

            List<String> destinations = accessor.getNativeHeader("destination");
            if (destinations != null){
                destination = destinations.get(0);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(command).append("\n")
                .append("id:").append(subId).append("\n")
                .append("destination:").append(destination).append("\n")
                .append(new String((byte[]) message.getPayload()) );
        return stringBuilder.toString();
    }


}
