package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
*   类路径: com.lhpang.ac.vo.OrderProductVo
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-26 15:34
*/
@Data
public class OrderProductVo {

    private List<OrderItemVo> orderItemVoList;

    private BigDecimal productTotalPrice;

}
