package com.gseek.gs.service.inter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.pojo.data.AdminDO;
import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Isabella
 * @create: 2023-05-17 20:13
 **/
public interface AdminService extends UserDetailsService {
    AdminDO selectAdminByAdminName(String adminName);

    @Override
    UserDetails loadUserByUsername(String adminName) throws UsernameNotFoundException;
}
