package com.gseek.gs.service.impl;

import com.gseek.gs.common.OrdinaryUser;
import com.gseek.gs.dao.MoneyMapper;
import com.gseek.gs.dao.UserIdentificationMapper;
import com.gseek.gs.dao.UserInformationMapper;
import com.gseek.gs.dao.UserPasswordMapper;
import com.gseek.gs.exce.business.ParameterWrong;
import com.gseek.gs.pojo.Money;
import com.gseek.gs.pojo.UserIdentification;
import com.gseek.gs.pojo.UserInformation;
import com.gseek.gs.pojo.UserPassword;
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
    UserPasswordMapper userPasswordMapper;
    @Autowired
    UserIdentificationMapper userIdentificationMapper;
    @Autowired
    UserInformationMapper userInformationMapper;
    @Autowired
    MoneyMapper moneyMapper;

    /**
     * 一般用户权限
     * */
    private final String AUTHORITIE_ORDINARYUSER="USER";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(String userName,String password,String email, long registerTime)
        throws ParameterWrong {
        // 验证参数
        if (!StrUtil.checkUserName(userName)){
            log.debug("用户名不可用|"+userName);
            throw new ParameterWrong("username");
        }
        if (!StrUtil.checkPassword(password)){
            log.debug("密码不可用|"+password);
            throw new ParameterWrong("password");
        }
        if (!StrUtil.checkEmail(email)){
            log.debug("邮箱不可用|"+email);
            throw new ParameterWrong("email");
        }

        UserPassword userPassword=new UserPassword();
        userPassword.setUserName(userName);
        userPassword.setPassword(password);
        userPassword.setSalt(passwordUtil.gainSalt());

        UserInformation userInformation=new UserInformation();
        userInformation.setEmail(email);

        userPasswordMapper.insertUserPassword(userPassword);
        Integer userId=userPassword.getUserId();
        assert userId == null : "主键不回显，建议检查mapper中有无配置useGeneratedKeys";

        userInformation.setUserId(userPassword.getUserId());
        userInformationMapper.insertUserInformation(userInformation);

        UserIdentification userIdentification=new UserIdentification(userPassword.getUserId());
        userIdentification.setModifiedTime(registerTime);
        userIdentificationMapper.insertUserIdentification(userIdentification);

        Money money=new Money(userPassword.getUserId());
        moneyMapper.insertMoney(money);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AUTHORITIE_ORDINARYUSER));
        OrdinaryUser user=new OrdinaryUser(username,authorities);

        UserPassword userPassword=userPasswordMapper.selectUserPasswordByUsername(username);
        if (userPassword==null){
            throw new UsernameNotFoundException("UsernameNotFound:"+username);
        }
        user.setPassword(userPassword.getPassword());
        return user;
    }
}
