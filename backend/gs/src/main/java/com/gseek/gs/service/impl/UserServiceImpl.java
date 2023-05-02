package com.gseek.gs.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对应/user/**的操作.
 *
 * @author Phak
 * @since 2023/5/2-19:04
 */
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
     * 注册一般用户.
     *
     * @param userName 用户名
     * @param password 密码
     * @param email 邮箱
     * @param registerTime 注册时间
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(String userName,String password,String email, long registerTime)
        throws ParameterWrong {
        // 验证参数
        if (!StrUtil.checkUserName(userName)){
            throw new ParameterWrong("username");
        }
        if (!StrUtil.checkPassword(password)){
            throw new ParameterWrong("password");
        }
        if (!StrUtil.checkEmail(email)){
            throw new ParameterWrong("email");
        }

        UserPassword userPassword=new UserPassword();
        userPassword.setPassword(password);
        userPassword.setSalt(passwordUtil.gainSalt());

        UserInformation userInformation=new UserInformation();
        userInformation.setUserName(userName);
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
}
