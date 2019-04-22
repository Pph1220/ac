package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类路径: com.lhpang.ac.vo.ProdectListVo
 * 描述: //TODO
 * @author: lhpang
 * @date: 2019-04-22 22:47
 */
@Data
public class ProductListVo {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;
}
