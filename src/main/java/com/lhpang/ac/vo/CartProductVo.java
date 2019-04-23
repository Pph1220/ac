package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
*   类路径: com.lhpang.ac.vo.CartProductVo
*   描述: 购物车商品VO
*   @author: lhpang
*   @date: 2019-04-23 14:25
*/
@Data
public class CartProductVo {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal productPrice;

    private Integer productStatus;

    private BigDecimal productTotalPrice;

    private Integer productStock;

    private Integer productChecked;

    private String limitQuantity;

}
