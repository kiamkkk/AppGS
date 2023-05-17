package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Phak
 * @since 2023/5/12-0:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFavDTO implements DTOPerService{

    private Integer goodId;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean=new ParameterWrongBean();

        if (goodId==null || goodId <= 0){
            bean.addParameters("商品id", goodId+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }

}
