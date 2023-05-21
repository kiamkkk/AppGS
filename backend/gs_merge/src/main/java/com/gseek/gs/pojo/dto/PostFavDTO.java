package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/11-23:43
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFavDTO implements DTOPerService{

    private Integer goodId;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();
        if (time==null || time <= 0){
            bean.addParameters("time", time+"");
        }
        if (goodId==null || goodId <= 0){
            bean.addParameters("goodId", goodId+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
