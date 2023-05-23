package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.pojo.dto.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 对应/user/**的操作.
 *
 * @author Phak
 * @since 2023/5/2-19:05
 */
public interface UserService extends UserDetailsService {
//todo 补充注释
    String PREFIX="[";
    String SUFFIX="]";
    /**
     * 注册一般用户.
     *
     * @param userName 用户名
     * @param rawPassword 密码
     * @param email 邮箱
     * @param registerTime 注册时间
     * */
    @Transactional(rollbackFor = Exception.class)
    String register(RegisterDTO dto) throws JsonProcessingException;

    /**
     * 获取用户一般信息.
     *
     * @param userId 用户id
     * @return 结果
     * */
    String getUserInformation(int userId) throws JsonProcessingException;

    /**
     * 获取用户实名认证信息.
     *
     * @param userId 用户id
     * @return json,含有idNumber（身份证号,加密）
     * */
    String getRealNameInformation(int userId) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException;

    /**
     * 修改用户一般信息.
     *
     * @param userId 用户id
     * @param email 邮箱
     * @param patchName 修改名称
     * @param photoPath 头像url地址
     * @return 通用响应
     * */
    String patchUserInformation(int userId, String originUserName,String photoPath,PatchUserInformationDTO dto) throws JsonProcessingException;

    /**
     * 修改用户实名认证信息.
     *
     * @param userId 用户id
     * @param rawIdNumber 身份证号（未解密）
     * @param postTime
     * @return 通用响应
     * */
    //todo 修改dto，把userId放进去
    String postRealNameInformation(int userId, PostRealNameInformationDTO dto) throws JsonProcessingException;

    /**
     * 从数据库中获取登录信息.
     *
     * @param username
     * @return OrdinaryUser 含有username、password、userId
     * @throws UsernameNotFoundException 没有查询到该用户名
     * */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
