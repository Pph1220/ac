package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
*   类路径: com.lhpang.ac.controller.protal.OrderController
*   描述: 订单Controller
*   @author: lhpang
*   @date: 2019-04-25 11:45
*/
@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    @ResponseBody
    @RequestMapping(value = "pay",method = RequestMethod.GET)
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        return orderService.pay(orderNo,user.getId(),path);
    }
    /**
     * 描 述: 阿里回调方法
     * @date: 2019-04-25 17:57
     * @author: lhpang
     * @param: [request]
     * @return: java.lang.Object
     **/
    @ResponseBody
    @RequestMapping(value = "aliPayCallback",method = RequestMethod.GET)
    public Object aliPayCallback(HttpServletRequest request){

        Map map = request.getParameterMap();

        ServerResponse response = orderService.aliPayCallBack(map);
        if(response.isSuccess()){
            return Constant.AlipayCallback.RESPONSE_SUCCESS;
        }

        return Constant.AlipayCallback.RESPONSE_FAILED;
    }
    
}
