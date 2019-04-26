package com.lhpang.ac.service;

import com.github.pagehelper.PageInfo;
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
    /**
     * 描 述: 查询订单支付状态
     * @date: 2019-04-26 10:12
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
    /**
     * 描 述: 购买
     * @date: 2019-04-26 11:55
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse create(Integer userId, Integer shippingId);
    /**
     * 描 述: 取消订单
     * @date: 2019-04-26 15:31
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> cancle(Integer userId,Long orderNo);
    /**
     * 描 述: 订单详情
     * @date: 2019-04-26 15:54
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse detail(Integer userId,Long orderNo);
    /**
     * 描 述: 分页查询订单列表
     * @date: 2019-04-26 16:14
     * @author: lhpang
     * @param: [userId, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
    /**
     * 描 述: 后台订单列表
     * @date: 2019-04-26 16:29
     * @author: lhpang
     * @param: [pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    ServerResponse<PageInfo> managerList(int pageNum,int pageSize);
    /**
     * 描 述:后台订单详情
     * @date: 2019-04-26 16:36
     * @author: lhpang
     * @param: [orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse managerDetail(Long orderNo);
    /**
     * 描 述: 后台通过orderNo搜索
     * @date: 2019-04-26 17:08
     * @author: lhpang
     * @param: [orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse search(Long orderNo);
}
