package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author Phak
 * @since 2023/5/23-13:34
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostChatImgDTO implements DTOPerService{

    private MultipartFile picture;
    private Long time;
    private Integer toUserId;


    public PostChatImgDTO(HttpServletRequest request) throws JsonProcessingException {
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        this.picture=params.getFile("picture");
        if (picture == null || picture.getOriginalFilename().isBlank() ){
            picture = null;
        }
        try {
            // 捕获类型转化异常
            this.time= Long.valueOf(params.getParameter("time"));
        }catch (ClassCastException e){
            throw new ParameterWrongException(new ParameterWrongBean().addParameters("time",params.getParameter("time")));
        }
        try {
            // 捕获类型转化异常
            this.toUserId= Integer.valueOf(params.getParameter("toUserId"));
        }catch (ClassCastException e){
            throw new ParameterWrongException(new ParameterWrongBean().addParameters("time",params.getParameter("toUserId")));
        }



    }

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        // 验参
        ParameterWrongBean bean =new ParameterWrongBean();

        if (toUserId == null || toUserId<=0){
            bean.addParameters("time",toUserId+"");
        }
        if (picture == null){
            bean.addParameters("picture","图片");
        }
        if (time == null || time<=0){
            bean.addParameters("time",time+"");
        }

        if (! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
