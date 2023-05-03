package com.gseek.gs.service.inter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对应/user/**的操作.
 *
 * @author Phak
 * @since 2023/5/2-19:05
 */
public interface UserService extends UserDetailsService {

    /**
     * 注册一般用户.
     *
     * @param userName 用户名
     * @param password 密码
     * @param email 邮箱
     * @param registerTime 注册时间
     * */
    @Transactional(rollbackFor = Exception.class)
    void register(String userName,String password,String email,long registerTime);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
