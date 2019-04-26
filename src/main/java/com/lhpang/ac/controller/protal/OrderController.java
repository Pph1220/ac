package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.service.UserService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@Slf4j
@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;


    /**
     * 描 述: 购买
     * @date: 2019-04-26 14:36
     * @author: lhpang
     * @param: [session, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "create",method = RequestMethod.GET)
    public ServerResponse create(HttpSession session,Integer shippingId){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.create(user.getId(), shippingId);
    }
    /**
     * 描 述: 取消订单
     * @date: 2019-04-26 15:20
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "cancle",method = RequestMethod.GET)
    public ServerResponse cancle(HttpSession session,long orderNo){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.cancle(user.getId(),orderNo);
    }
    /**
     * 描 述: 订单详情
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public ServerResponse detail(HttpSession session,Long orderNo){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.detail(user.getId(), orderNo);
    }
    /**
     * 描 述: 分页查询订单列表
     * @date: 2019-04-26 16:15
     * @author: lhpang
     * @param: [session, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.list(user.getId(),pageNum,pageSize);
    }


    /**
     * 描 述: 支付接口
     * @date: 2019-04-26 10:43
     * @author: lhpang
     * @param: [session, orderNo, request]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
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
    @RequestMapping(value = "aliPayCallback",method = RequestMethod.POST)
    public Object aliPayCallback(HttpServletRequest request){

        Map map = request.getParameterMap();

        ServerResponse response = orderService.aliPayCallBack(map);
        if(response.isSuccess()){
            log.info("回调成功");
            return Constant.AlipayCallback.RESPONSE_SUCCESS;
        }
        log.info("回调失败");
        return Constant.AlipayCallback.RESPONSE_FAILED;
    }
    /**
     * 描 述: 查询订单支付状态
     * @date: 2019-04-26 10:16
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Boolea>
     **/
    @ResponseBody
    @RequestMapping(value = "queryPayStatus",method = RequestMethod.GET)
    public ServerResponse<Boolean> queryPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.queryOrderPayStatus(user.getId(), orderNo).isSuccess() ? ServerResponse.createBySuccess(true) : ServerResponse.createBySuccess(false);
    }
    
}
