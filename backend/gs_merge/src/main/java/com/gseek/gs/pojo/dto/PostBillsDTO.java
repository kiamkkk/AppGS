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
 * @since 2023/5/12-20:53
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostBillsDTO implements DTOPerService{

    private Long time;
    private Integer goodId;
    private Integer sellerId;
    private Integer buyerId;
    private BigDecimal price;

    @Override
    public void validateParameters() throws ParameterWrongException, JsonProcessingException {
        ParameterWrongBean bean=new ParameterWrongBean();
        if (time==null || time <= 0){
            bean.addParameters("time", time+"");
        }
        if (goodId==null || goodId <= 0){
            bean.addParameters("goodId", goodId+"");
        }
        if (sellerId==null || sellerId <= 0){
            bean.addParameters("sellerId", sellerId+"");
        }
        if (buyerId==null || buyerId <= 0){
            bean.addParameters("buyerId", buyerId+"");
        }
        if (price==null || price.compareTo(BigDecimal.valueOf(0.00)) <= 0){
            bean.addParameters("price", price+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
