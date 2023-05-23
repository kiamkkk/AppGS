package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.pojo.dto.RegisterDTO;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.MinioUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.Objects;

/**
 * 对应 /users/** 下的操作
 *
 * @author Phak
 * @since 2023/5/2-19:00
 */

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    Result result;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    MinioUtil minioUtil;

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto)
            throws JsonProcessingException, ParameterWrongException, IllegalBlockSizeException, BadPaddingException {
        log.info("开始注册");
        dto.perService();
        return userService.register(dto);
    }

    @GetMapping("/{username}")
    public String getUserInformation(@PathVariable("username") String userName,
                                     @CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws ServerException, JsonProcessingException {
        log.info("开始获取用户信息");
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return userService.getUserInformation(details.getUserId());

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @GetMapping("/{username}/real_name")
    public String getRealNameInformation(@PathVariable("username") String userName,
                                         @CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        if (!Objects.equals(authentication.getName(), userName)){
            throw new ForbiddenException();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            int userId=details.getUserId();
            return userService.getRealNameInformation(userId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    /**
     * @RequestBody 只接受json，这里有可能接收到文件，只能用multipart/form-data，得自己接参数手动装配
     *
     * */
    @PatchMapping("/{username}")
    public String patchUserInformation(@PathVariable("username") String userName,
                                       @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                       HttpServletRequest request)

            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException, com.gseek.gs.exce.ServerException  {

        PatchUserInformationDTO dto=new PatchUserInformationDTO(request);
        dto.perService();

        if (!Objects.equals(authentication.getName(), userName)){
            throw new ForbiddenException();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            int userId=details.getUserId();
            String photoPath=minioUtil.changeProfilePhoto(userId, dto.getPicture());
            return userService.patchUserInformation(userId,photoPath,dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }

    @PostMapping("/{username}/real_name")
    public String postRealNameInformation(@PathVariable("username") String userName,
                                          @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                          @RequestBody PostRealNameInformationDTO dto)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        if (!Objects.equals(authentication.getName(), userName)){

            throw new ForbiddenException();

        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            return userService.postRealNameInformation(details.getUserId(),dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }


}
