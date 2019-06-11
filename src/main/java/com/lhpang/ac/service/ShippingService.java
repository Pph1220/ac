package com.lhpang.ac.service;

import com.github.pagehelper.PageInfo;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Shipping;

/**
 * 类路径: com.lhpang.ac.service.ShippingService
 * 描述: 收货地址Service
 *
 * @author: lhpang
 * @date: 2019-04-17 10:13
 */
public interface ShippingService {
    /**
     * 描 述: 新增收货地址
     *
     * @date: 2019-04-24 10:14
     * @author: lhpang
     * @param: [userId, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 描 述: 删除收货地址
     *
     * @date: 2019-04-24 10:24
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse delete(Integer userId, Integer shippingId);

    /**
     * 描 述: 更新收货地址
     *
     * @date: 2019-04-24 10:41
     * @author: lhpang
     * @param: [userId, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse update(Integer userId, Shipping shipping);

    /**
     * 描 述: 查询收货地址详情
     *
     * @date: 2019-04-24 10:52
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.Shipping>
     **/
    ServerResponse<Shipping> detail(Integer userId, Integer shippingId);

    /**
     * 描 述: 查询收货地址列表
     *
     * @date: 2019-04-24 11:00
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<List>
     **/
    ServerResponse list(Integer userId);
}
