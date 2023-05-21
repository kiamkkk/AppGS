package com.gseek.gs.pojo.data;

import com.gseek.gs.pojo.dto.PostRealNameInformationDTO;
import com.gseek.gs.pojo.dto.RegisterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * user_identification实体类
 *
 * @author Phak
 * @since 2023/5/2-20:06
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentificationDO {


    private Integer userId;
    private String idNumber;
    private Boolean certified;
    private Long modifiedTime;

    public UserIdentificationDO(RegisterDTO dto, Integer userId) {
        this.userId = userId;
        this.modifiedTime=dto.getTime();
    }

    public UserIdentificationDO(PostRealNameInformationDTO dto, Integer userId) {
        this.userId = userId;
        this.modifiedTime=dto.getTime();
        this.idNumber=dto.getIdNumber();
    }
}
