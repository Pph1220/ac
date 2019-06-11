package com.lhpang.ac.service;

import com.github.pagehelper.PageInfo;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.vo.ProductDetailVo;

/**
 * 类路径: com.lhpang.ac.service.ProductService
 * 描述: 商品Service
 *
 * @author: lhpang
 * @date: 2019-04-17 10:14
 */
public interface ProductService {
    /**
     * 描 述: 商品新增或修改
     *
     * @date: 2019-04-22 16:36
     * @author: lhpang
     * @param: [product]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> saveOrUpdateProduct(Product product);

    /**
     * 描 述: 修改上下架状态
     *
     * @date: 2019-04-22 16:43
     * @author: lhpang
     * @param: [id]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> setStatus(Integer id);

    /**
     * 描 述: 商品详情
     *
     * @date: 2019-04-22 17:53
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Object>
     **/
    ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

    /**
     * 描 述: 查询Product列表
     *
     * @date: 2019/4/22 22:58
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 描 述: 搜索商品
     *
     * @date: 2019/4/22 23:30
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> searchProduct(String productName, int pageNum, int pageSize);

    /**
     * 描 述: 获得商品信息详情(前台)
     *
     * @date: 2019-04-23 10:18
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.ProductDetailVo>
     **/
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 描 述: 搜索(前台)
     *
     * @date: 2019-04-23 10:57
     * @author: lhpang
     * @param: [productName, pageSize, pageNum, orderBy]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> getProductByproductNameCategoryId(String productName, Integer categoryId, int pageSize, int pageNum, String orderBy);

    /**
     * 描 述: 全部商品（前台）
     *
     * @date: 2019/4/29 22:34
     * @author: lhpang
     * @param: [pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> list(int pageNum, int pageSize);

    /**
     * 描 述: 后台删除商品
     *
     * @date: 2019/5/6 12:03
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse delete(Integer productId);
}
