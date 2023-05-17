package com.gseek.gs.pojo.dto;

import com.gseek.gs.exce.business.ParameterWrongException;
import com.gseek.gs.pojo.business.ParameterWrongBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Phak
 * @since 2023/5/12-20:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBillsDTO implements DTOPerService{

    private Long time;
    private Integer goodId;
    private Integer sellerId;
    private Integer buyerId;
    private BigDecimal price;

    @Override
    public void validateParameters() throws ParameterWrongException {
        ParameterWrongBean bean=new ParameterWrongBean();
        if (time==null || time <= 0){
            bean.addParameters("订单提交时间", time+"");
        }
        if (goodId==null || goodId <= 0){
            bean.addParameters("商品id", goodId+"");
        }
        if (sellerId==null || sellerId <= 0){
            bean.addParameters("卖家id", sellerId+"");
        }
        if (buyerId==null || buyerId <= 0){
            bean.addParameters("买家id", buyerId+"");
        }
        if (price==null || price.compareTo(BigDecimal.valueOf(0.00)) <= 0){
            bean.addParameters("成交价", price+"");
        }

        if(! bean.getWrongParameters().isEmpty()){
            throw new ParameterWrongException(bean);
        }
    }
}
