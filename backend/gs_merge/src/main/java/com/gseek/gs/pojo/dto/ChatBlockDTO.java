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
 * @since 2023/5/17-9:33
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBlockDTO implements DTOPerService{

    private Integer toUserId;
    private Integer fromUserId;
    private Long time;
    private Boolean block;


    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();

        if (toUserId==null || toUserId <= 0){
            bean.addParameters("goodId", toUserId+"");
        }
        if (fromUserId==null || fromUserId==0){
            bean.addParameters("fromUserId", fromUserId+"");
        }
        if (time==null || time==0){
            bean.addParameters("time", time+"");
        }
        if (block==null){
            bean.addParameters("block", block+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
