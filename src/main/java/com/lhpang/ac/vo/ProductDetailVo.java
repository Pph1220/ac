package com.lhpang.ac.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类路径: com.lhpang.ac.vo.ProductDetailVo
 * 描述: 商品详情vo
 *
 * @author: lhpang
 * @date: 2019-04-22 20:49
 */
@Data
public class ProductDetailVo {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;
}
