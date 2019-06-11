package com.lhpang.ac.vo;

import lombok.Data;

/**
 * 类路径: com.lhpang.ac.vo.ShippingVo
 * 描述: 收货地址Vo
 *
 * @author: lhpang
 * @date: 2019-04-26 13:52
 */
@Data
public class ShippingVo {

    private int id;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

}
