package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.common.ForbiddenException;
import com.gseek.gs.exce.business.common.ParameterWrongException;
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
 * 处理用户基本信息.
 *
 * @author Phak
 * @since 2023/5/2-19:00
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController implements Controller{

    @Autowired
    Result result;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    MinioUtil minioUtil;

    /**
     * 获取用户一般信息
     * */
    @GetMapping("/{username}")
    public String getUserInformation(@PathVariable("username") String userName,
                                     @CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws ServerException, JsonProcessingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        return userService.getUserInformation(details.getUserId());
    }

    /**
     * 获取用户实名认证信息
     * */
    @GetMapping("/{username}/real_name")
    public String getRealNameInformation(@PathVariable("username") String userName,
                                         @CurrentSecurityContext(expression = "Authentication") Authentication authentication)
            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        CustomWebAuthenticationDetails details =perService(authentication);
        // 鉴权
        if (!Objects.equals(authentication.getName(), userName)){
            throw ForbiddenException.gainNotAccess();
        }

        return userService.getRealNameInformation(details.getUserId());
    }

    /**
     * 注册.
     * */
    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto)
            throws JsonProcessingException, ParameterWrongException, IllegalBlockSizeException, BadPaddingException {
        dto.perService();
        return userService.register(dto);
    }

    /**
     * 上传实名验证信息.
     * */
    @PostMapping("/{username}/real_name")
    public String postRealNameInformation(@PathVariable("username") String userName,
                                          @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                          @RequestBody PostRealNameInformationDTO dto)
            throws JsonProcessingException, ForbiddenException, ServerException {
        CustomWebAuthenticationDetails details =perService(authentication);
        dto.perService();
        // 鉴权
        if (!Objects.equals(authentication.getName(), userName)){
            throw ForbiddenException.gainNotAccess();
        }

        return userService.postRealNameInformation(details.getUserId(),dto);
    }

    /**
     * 修改用户信息.
     * Content-Type:multipart/form-data
     * */
    @PatchMapping("/{username}")
    public String patchUserInformation(@PathVariable("username") String userName,
                                       @CurrentSecurityContext(expression = "Authentication") Authentication authentication,
                                       HttpServletRequest request)

            throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException, com.gseek.gs.exce.ServerException  {
        CustomWebAuthenticationDetails details =perService(authentication);
        PatchUserInformationDTO dto=new PatchUserInformationDTO(request);
        dto.perService();
        // 鉴权
        if (!Objects.equals(authentication.getName(), userName)){
            throw ForbiddenException.gainNotAccess();
        }

        int userId=details.getUserId();
        String photoPath=minioUtil.changeProfilePhoto(userId, dto.getPicture());
        return userService.patchUserInformation(userId,photoPath,dto);
    }

}
