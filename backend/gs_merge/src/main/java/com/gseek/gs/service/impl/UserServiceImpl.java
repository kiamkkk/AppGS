package com.gseek.gs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gseek.gs.common.Result;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.dao.UserIdentificationMapper;
import com.gseek.gs.dao.UserInformationMapper;
import com.gseek.gs.dao.UserPasswordMapper;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.OrdinaryUser;
import com.gseek.gs.pojo.business.UserIdentificationBO;
import com.gseek.gs.pojo.business.UserInformationBO;
import com.gseek.gs.pojo.data.MoneyDO;
import com.gseek.gs.pojo.data.UserIdentificationDO;
import com.gseek.gs.pojo.data.UserInformationDO;
import com.gseek.gs.pojo.data.UserPasswordDO;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.service.inter.UserService;
import com.gseek.gs.util.PasswordUtil;
import com.gseek.gs.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 一般用户权限
     * */
    private final String AUTHORITIE_ORDINARYUSER="USER";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(String userName,String rawPassword,String email, long registerTime)
        throws ParameterWrongException {
        // 验证参数
        if (!StrUtil.checkUserName(userName)){
            log.debug("用户名|"+userName+"\n");
            throw new ParameterWrongException("username");
        }
        if (!StrUtil.checkPassword("password")){
            log.debug("密码|"+"password"+"\n");
            throw new ParameterWrongException("password");
        }
        if (!StrUtil.checkEmail(email)){
            log.debug("邮箱|"+email+"\n");
            throw new ParameterWrongException("email");
        }

        UserPasswordDO userPasswordDO =new UserPasswordDO();
        userPasswordDO.setUserName(userName);
        //todo 记得加密
        userPasswordDO.setPassword("password");
        userPasswordDO.setSalt(PasswordUtil.gainSalt());

        UserInformationDO userInformationDO =new UserInformationDO();
        userInformationDO.setEmail(email);

        userPasswordMapper.insertUserPassword(userPasswordDO);
        Integer userId= userPasswordDO.getUserId();
        assert userId == null : "主键不回显，建议检查mapper中有无配置useGeneratedKeys";

        userInformationDO.setUserId(userPasswordDO.getUserId());
        userInformationMapper.insertUserInformation(userInformationDO);

        UserIdentificationDO userIdentificationDO =new UserIdentificationDO(userPasswordDO.getUserId());
        userIdentificationDO.setModifiedTime(registerTime);
        userIdentificationMapper.insertUserIdentification(userIdentificationDO);

        MoneyDO moneyDO =new MoneyDO(userPasswordDO.getUserId());
        moneyMapper.insertMoney(moneyDO);

    }

    @Override
    public String getUserInformation(int userId) throws JsonProcessingException {

        UserInformationBO bo =userInformationMapper.selectUserInformationByUserId(userId);
        return objectMapper.writeValueAsString(bo);

    }

    @Override
    public String getRealNameInformation(int userId) throws JsonProcessingException {
        UserIdentificationBO bo =userIdentificationMapper.selectUserIdentificationByUserId(userId);
        return objectMapper.writeValueAsString(bo);
    }

    @Override
    public String patchUserInformation(int userId, String photoPath, PatchUserInformationDTO dto)
            throws JsonProcessingException {
        UserPasswordDO userPasswordDO=new UserPasswordDO(userId);
        UserInformationDO userInformationDO=new UserInformationDO(userId);

        if (dto.getEmail()!=null){
            userInformationDO.setEmail(dto.getEmail());
        }
        if (photoPath!=null){
            userInformationDO.setProfilePhoto(photoPath);
        }
        if (dto.getUsername()!=null){
            userPasswordDO.setUserName(dto.getUsername());
        }
        userInformationMapper.updateUserInformation(userInformationDO);
        userPasswordMapper.updateUserPassword(userPasswordDO);

        return result.gainPatchSuccess();
    }

    @Override
    public String postRealNameInformation(int userId, PostRealNameInformationDTO postRealNameInformationDTO) throws JsonProcessingException {
        UserIdentificationBO bo = userIdentificationMapper.selectUserIdentificationByUserId(userId);
        return objectMapper.writeValueAsString(bo);
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
        user.setPassword(userPasswordDO.getPassword());
        user.setUserId(userPasswordDO.getUserId());
        return user;
    }
}
