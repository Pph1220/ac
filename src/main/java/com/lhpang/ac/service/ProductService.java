package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Product;

/**
*   类路径: com.lhpang.ac.service.ProductService
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-17 10:14
*/
public interface ProductService {
    /**
     * 描 述: 商品新增或修改
     * @date: 2019-04-22 16:36
     * @author: lhpang
     * @param: [product]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> setStatus(Integer id);
    
}
