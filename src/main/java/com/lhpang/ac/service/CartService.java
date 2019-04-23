package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;

/**
*   类路径: com.lhpang.ac.service.CartService
*   描述: 购物车Service
*   @author: lhpang
*   @date: 2019-04-17 10:15
*/
public interface CartService {

    /**
     * 描 述: 加入购物车
     * @date: 2019-04-23 14:38
     * @author: lhpang
     * @param: [userId, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse add(Integer userId, Integer count , Integer productId);
}
