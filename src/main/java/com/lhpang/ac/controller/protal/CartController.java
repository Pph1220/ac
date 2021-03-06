package com.lhpang.ac.controller.protal;

import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.CartService;
import com.lhpang.ac.service.ShippingService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 类路径: com.lhpang.ac.controller.protal.CartController
 * 描述: 购物车Controller
 *
 * @author: lhpang
 * @date: 2019-04-23 13:57
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShippingService shippingService;

    /**
     * 描 述: 加入购物车
     *
     * @date: 2019-04-23 17:06
     * @author: lhpang
     * @param: [session, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.add(user.getId(), count, productId);
    }

    /**
     * 描 述: 更新购物车商品
     *
     * @date: 2019/4/23 22:00
     * @author: lhpang
     * @param: [session, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.update(user.getId(), count, productId);
    }

    /**
     * 描 述: 删除购物车中的商品
     *
     * @date: 2019/4/23 22:35
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping(value = "delete")
    public ServerResponse delete(HttpSession session, String productId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.delete(user.getId(), productId);
    }

    /**
     * 描 述: 查询购物车
     *
     * @date: 2019/5/1 16:01
     * @author: lhpang
     * @param: [session]
     * @return: org.springframework.web.servlet.ModelAndView
     **/
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView list(HttpSession session) {

        Map<String, Object> result = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            result.put("result", response);
            return new ModelAndView("common/fail", result);
        }
        //查询购物车中商品
        ServerResponse cartVo = cartService.list(user.getId());
        //查询收货地址
        ServerResponse shippingVoList = shippingService.list(user.getId());
        result.put("cartVo", cartVo);
        result.put("shippingVoList", shippingVoList);

        ModelAndView modelAndView = new ModelAndView("portal/cart/cartList");


        return modelAndView.addObject("result", result);
    }

    /**
     * 描 述: 全选
     *
     * @date: 2019/4/23 23:18
     * @author: lhpang
     * @param: [session]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @ResponseBody
    @RequestMapping(value = "checkAll", method = RequestMethod.GET)
    public ServerResponse<CartVo> checkAll(HttpSession session) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.checkOrUnCheckProduct(user.getId(), null, Constant.Cart.CHECKED);
    }

    /**
     * 描 述: 全不选
     *
     * @date: 2019/4/23 23:18
     * @author: lhpang
     * @param: [session]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @ResponseBody
    @RequestMapping(value = "unCheckAll", method = RequestMethod.GET)
    public ServerResponse<CartVo> unCheckAll(HttpSession session) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.checkOrUnCheckProduct(user.getId(), null, Constant.Cart.UN_CHECKED);
    }

    /**
     * 描 述: 选择
     *
     * @date: 2019/4/23 23:18
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @ResponseBody
    @RequestMapping(value = "check", method = RequestMethod.GET)
    public ServerResponse<CartVo> check(HttpSession session, Integer productId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.checkOrUnCheckProduct(user.getId(), productId, Constant.Cart.CHECKED);
    }

    /**
     * 描 述: 不选择
     *
     * @date: 2019/4/23 23:18
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @ResponseBody
    @RequestMapping(value = "unCheck", method = RequestMethod.GET)
    public ServerResponse<CartVo> unCheck(HttpSession session, Integer productId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return cartService.checkOrUnCheckProduct(user.getId(), productId, Constant.Cart.UN_CHECKED);
    }

    @ResponseBody
    @RequestMapping(value = "getCartProductCount", method = RequestMethod.GET)
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if (!response.isSuccess()) {
            return ServerResponse.createBySuccess(0);
        }

        return cartService.getCartProductCount(user.getId());
    }

}
