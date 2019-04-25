package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;

import java.util.Map;

/**
*   类路径: com.lhpang.ac.service.OrderService
*   描述: 订单Service
*   @author: lhpang
*   @date: 2019-04-17 10:15
*/
public interface OrderService {
    /**
     * 描 述: 核心支付方法
     * @date: 2019-04-25 15:58
     * @author: lhpang
     * @param: [orderNo, userId, path]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse pay(Long orderNo, Integer userId, String path);
    /**
     * 描 述: 阿里回调方法
     * @date: 2019-04-25 16:52
     * @author: lhpang
     * @param: [map]
     * @return: java.lang.Object
     **/
    ServerResponse aliPayCallBack(Map requestMap);
    
}
