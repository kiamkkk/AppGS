package com.gseek.gs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.config.login.handler.CustomWebAuthenticationDetails;
import com.gseek.gs.exce.ServerException;
import com.gseek.gs.exce.business.ForbiddenException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.MinioUtil;
import com.gseek.gs.util.PasswordUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.sql.SQLException;
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
    public String register(@RequestBody String json, HttpServletResponse response)
            throws JsonProcessingException, ParameterWrongException, SQLException, IllegalBlockSizeException, BadPaddingException {
        //todo 这个方法要规范，格式参考下面
        JsonNode jsonNode=objectMapper.readTree(json);
        String userName=jsonNode.get("userName").asText();
        //todo 解密放在service里
        String rawPassword=PasswordUtil.decrypt(jsonNode.get("password").asText());
        log.debug("解密后密码|"+rawPassword);
        String email=jsonNode.get("email").asText();
        long time=jsonNode.get("time").asLong();

        userService.register(userName,rawPassword,email,time);

        return result.gainPostSuccess();
    }

    @GetMapping("/{username}")
    public String getUserInformation(@PathVariable("username") String userName,
                                     @CurrentSecurityContext(expression = "authentication")
                                        Authentication authentication)
            throws ServerException, JsonProcessingException {
        if (!Objects.equals(authentication.getName(), userName)){
            throw new ForbiddenException();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            int userId=details.getUserId();
            return userService.getUserInformation(userId);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @GetMapping("/{username}/real_name")
    public String getRealNameInformation(@PathVariable("username") String userName,
                                         @CurrentSecurityContext(expression = "authentication")
                                         Authentication authentication) throws JsonProcessingException {
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

    @PatchMapping("/{username}")
    public String patchUserInformation(@PathVariable("username") String userName,
                                       @CurrentSecurityContext(expression = "authentication")
                                       Authentication authentication,
                                       PatchUserInformationDTO dto) throws JsonProcessingException {
        //todo service操作放进service里

        if (!Objects.equals(authentication.getName(), userName)){
            throw new ForbiddenException();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){

            int userId=details.getUserId();
            //todo 不要验空，交给saveProfilePhoto设置默认头像
            String photoPath=minioUtil.saveProfilePhoto(userId, dto.getPicture());
            return userService.patchUserInformation(userId,photoPath,dto);

        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }

    }

    @PostMapping("/{username}/real_name")
    public String postRealNameInformation(@PathVariable("username") String userName,
                                          @RequestBody String json,
                                          @CurrentSecurityContext(expression = "authentication")
                                          Authentication authentication) throws JsonProcessingException {
        if (!Objects.equals(authentication.getName(), userName)){
            throw new ForbiddenException();
        }
        if (authentication.getDetails() instanceof CustomWebAuthenticationDetails details){
            //todo 直接接收RealNameInformationDTO
            PostRealNameInformationDTO dto=objectMapper.readValue(json, PostRealNameInformationDTO.class);
            int userId=details.getUserId();
            return userService.postRealNameInformation(userId,dto);
        }else {
            log.error("向下转型失败|不能将authentication中的detail转为CustomWebAuthenticationDetails");
            throw new ServerException("认证时出错");
        }
    }
}
