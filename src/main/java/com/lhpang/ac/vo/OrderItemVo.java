package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类路径: com.lhpang.ac.vo.OrderItemVo
 * 描述: OrderItemVo
 *
 * @author: lhpang
 * @date: 2019-04-26 13:49
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;

    private String updateTime;

}
