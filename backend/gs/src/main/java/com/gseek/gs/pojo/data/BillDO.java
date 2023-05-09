package com.gseek.gs.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Phak
 * @since 2023/5/9-13:13
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BillDO {

    private Integer billId;

    private Integer goodId;

    private Long createTime;

    private Boolean completed;

    private Integer sellerId;

    private Integer buyerId;

    private Boolean inspected;

    private Long inspectedTime;

}
