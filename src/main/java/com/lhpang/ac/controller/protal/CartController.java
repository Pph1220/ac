package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.CartService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.protal.CartController
*   描述: 购物车Controller
*   @author: lhpang
*   @date: 2019-04-23 13:57
*/
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    /**
     * 描 述: 加入购物车
     * @date: 2019-04-23 17:06
     * @author: lhpang
     * @param: [session, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping("add")
    public ServerResponse<CartVo> add(HttpSession session, Integer count , Integer productId){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return cartService.add(productId, count, productId);
    }

}
