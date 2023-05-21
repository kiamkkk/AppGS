package com.gseek.gs.pojo.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/11-22:19
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostOfferPriceDTO implements DTOPerService{
    private BigDecimal offerPrice;
    private Integer goodId;
    private Integer offerUserId;
    private Long time;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean =new ParameterWrongBean();


        if (offerUserId==null || offerUserId <= 0){
            bean.addParameters("offerUserId", offerUserId+"");
        }
        if (time==null || time <= 0){
            bean.addParameters("time", time+"");
        }
        if (goodId==null || goodId <= 0){
            bean.addParameters("goodId", goodId+"");
        }
        if (offerPrice==null || offerPrice.compareTo(BigDecimal.valueOf(0.00)) <=0){
            bean.addParameters("price", offerPrice+"");
        }

        if (! bean.getWrongParameters().isEmpty() ){
            throw new ParameterWrongException(bean);
        }
    }
}
