package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.bean.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/12-0:12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFavDTO implements DTOPerService{

    private Integer goodId;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();

        if (goodId==null || goodId <= 0){
            bean.addParameters("goodId", goodId+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }

}
