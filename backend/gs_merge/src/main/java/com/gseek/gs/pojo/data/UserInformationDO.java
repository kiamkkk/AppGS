package com.gseek.gs.pojo.data;

import com.gseek.gs.pojo.dto.PatchUserInformationDTO;
import com.gseek.gs.pojo.dto.RegisterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * user_information实体类
 *
 * @author Phak
 * @since 2023/5/2-19:54
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInformationDO {

    private Integer userId;
    private String email;
    private String profilePhoto;

    public UserInformationDO(RegisterDTO dto, Integer userId) {
        this.userId = userId;
        this.email=dto.getEmail();
    }

    public UserInformationDO(PatchUserInformationDTO dto,String photoPath,int userId) {
        if (dto.getEmail()!=null){
            this.email=dto.getEmail();
        }
        if (photoPath!=null){
            this.profilePhoto=photoPath;
        }
        if (dto.getUsername()!=null){
            this.userId=userId;
        }
    }
}
