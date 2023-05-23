package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.*;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.exce.business.users.RepeatUserNameException;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import com.gseek.gs.pojo.business.UserIdentificationBO;
import com.gseek.gs.pojo.business.UserInformationBO;
import com.gseek.gs.pojo.data.MoneyDO;
import com.gseek.gs.pojo.data.UserIdentificationDO;
import com.gseek.gs.pojo.data.UserInformationDO;
import com.gseek.gs.pojo.data.UserPasswordDO;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.pojo.dto.RegisterDTO;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对应/user/**的操作.
 *
 * @author Phak
 * @since 2023/5/2-19:04
 */
@Slf4j
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordUtil passwordUtil;
    @Autowired
    Result result;
    @Autowired
    UserPasswordMapper userPasswordMapper;
    @Autowired
    UserIdentificationMapper userIdentificationMapper;
    @Autowired
    UserInformationMapper userInformationMapper;
    @Autowired
    MoneyMapper moneyMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    GoodMapper goodMapper;



    /**
     * 一般用户权限
     * */
    private final String AUTHORITIE_ORDINARYUSER="USER";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(RegisterDTO dto)
            throws ParameterWrongException, JsonProcessingException {

        UserPasswordDO userPasswordDO =new UserPasswordDO(dto);
        userPasswordDO.setSalt(PasswordUtil.gainSalt());
        try{
            userPasswordMapper.insertUserPassword(userPasswordDO);
        }catch (DuplicateKeyException e) {
            throw new RepeatUserNameException(e);
        }
        Integer userId= userPasswordDO.getUserId();
        if (userId == null){
            log.error("主键不回显，建议检查mapper中有无配置useGeneratedKeys");
        }
        userInformationMapper.insertUserInformation(new UserInformationDO(dto,userId));
        userIdentificationMapper.insertUserIdentification(new UserIdentificationDO(dto,userId));
        moneyMapper.insertMoney(new MoneyDO(userId));
        log.info("结束注册");
        return result.gainPostSuccess();
    }

    @Override
    public String getUserInformation(int userId) throws JsonProcessingException {

        UserInformationBO bo =userInformationMapper.selectUserInformationByUserId(userId);
        log.info("结束获取用户信息");
        return objectMapper.writeValueAsString(bo);

    }

    @Override
    public String getRealNameInformation(int userId) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException {
        UserIdentificationBO bo =userIdentificationMapper.selectUserIdentificationByUserId(userId);
        bo.postService();
        return objectMapper.writeValueAsString(bo);
    }

    @Override
    public String patchUserInformation(int userId, String originUserName,String photoPath, PatchUserInformationDTO dto)
            throws JsonProcessingException {
        // todo 要支持修改密码！！！
        UserPasswordDO userPasswordDO=new UserPasswordDO(userId);
        UserInformationDO userInformationDO=new UserInformationDO(dto,photoPath,userId);

        userInformationMapper.updateUserInformation(userInformationDO);

        // 如果需要修改用户名，修改与用户名有关的字段
        // good中的own_user_name需要修改
        String patchUserName=dto.getUsername();
        if (patchUserName==null||patchUserName.isBlank()){
            goodMapper.updateOwnUserName(patchUserName,originUserName);
        }

       // userPasswordMapper.updateUserPassword(userPasswordDO);

        return result.gainPatchSuccess();
    }

    @Override
    public String postRealNameInformation(int userId, PostRealNameInformationDTO dto) throws JsonProcessingException {
        userIdentificationMapper.updateUserIdentification(new UserIdentificationDO(dto,userId));
        return result.gainPostSuccess();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AUTHORITIE_ORDINARYUSER));
        OrdinaryUser user=new OrdinaryUser(username,authorities);

        UserPasswordDO userPasswordDO =userPasswordMapper.selectUserPasswordByUsername(username);
        if (userPasswordDO ==null){
            log.debug("UsernameNotFound|"+username);
            throw new UsernameNotFoundException("UsernameNotFound:"+username);
        }

        user.setPassword(PREFIX+userPasswordDO.getSalt()+SUFFIX+userPasswordDO.getPassword());
        user.setUserId(userPasswordDO.getUserId());
        return user;
    }

}
