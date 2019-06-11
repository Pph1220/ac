package com.lhpang.ac.vo;

import com.lhpang.ac.pojo.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类路径: com.lhpang.ac.vo.OrderVo
 * 描述: 订单Vo
 *
 * @author: lhpang
 * @date: 2019-04-26 13:45
 */
@Data
public class OrderVo {

    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;

    private Integer status;

    private String statusDesc;

    private String paymentTime;

    private String dendTime;

    private String endTime;

    private String createTime;

    private String updateTime;
    /**
     * 订单明细
     */
    private List<OrderItemVo> orderItemVos;

    private Integer shippingId;

    private String receiverName;

    private ShippingVo shippingVo;

}
