package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.vo.CartVo;

/**
 * 类路径: com.lhpang.ac.service.CartService
 * 描述: 购物车Service
 *
 * @author: lhpang
 * @date: 2019-04-17 10:15
 */
public interface CartService {

    /**
     * 描 述: 加入购物车
     *
     * @date: 2019-04-23 14:38
     * @author: lhpang
     * @param: [userId, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    ServerResponse<CartVo> add(Integer userId, Integer count, Integer productId);

    /**
     * 描 述: 修改购物车中商品
     *
     * @date: 2019/4/23 21:55
     * @author: lhpang
     * @param: [userId, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    ServerResponse<CartVo> update(Integer userId, Integer count, Integer productId);

    /**
     * 描 述: 删除购物车中商品
     *
     * @date: 2019/4/23 22:02
     * @author: lhpang
     * @param: [userId, productIds]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    ServerResponse<CartVo> delete(Integer userId, String productIds);

    /**
     * 描 述: 查询购物车中的商品
     *
     * @date: 2019/4/23 22:38
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    ServerResponse<CartVo> list(Integer userId);

    /**
     * 描 述: 全选或全不选
     *
     * @date: 2019/4/23 23:26
     * @author: lhpang
     * @param: [userId, productId, checked]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    ServerResponse<CartVo> checkOrUnCheckProduct(Integer userId, Integer productId, Integer checked);

    /**
     * 描 述: 查询购物车中商品总数
     *
     * @date: 2019/4/23 23:50
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Integer>
     **/
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
