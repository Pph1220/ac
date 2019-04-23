package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
*   类路径: com.lhpang.ac.vo.CartVo
*   描述: 购物车vo
*   @author: lhpang
*   @date: 2019-04-23 14:30
*/
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalPrice;
    //是否已经都勾选
    private Boolean allChecked;
}
