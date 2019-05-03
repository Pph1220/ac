package com.lhpang.ac.controller.protal;

import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Order;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.utils.NumberUtil;
import com.lhpang.ac.vo.OrderVo;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
     * 描 述: 提交订单
     * @date: 2019-04-26 14:36
     * @author: lhpang
     * @param: [session, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @PostMapping("create")
    public ModelAndView create(HttpSession session, Integer shippingId){
        Map result = Maps.newHashMap();

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse checkOnLine = userService.checkOnLine(user);
        if(!checkOnLine.isSuccess()){
            result.put("result",checkOnLine);
            return new ModelAndView("common/fail",result);
        }
        ServerResponse create = orderService.create(user.getId(), shippingId);
        result.put("result",create);
        if(!create.isSuccess()){
            return new ModelAndView("common/fail",result);
        }

        ServerResponse detail = orderService.detail(user.getId(),((OrderVo)create.getData()).getOrderNo());
        result.put("result", detail);
        return new ModelAndView("portal/order/orderDetail",result);
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
     * @date: 2019/5/2 13:22
     * @author: lhpang
     * @param: [session, strOrderNo]
     * @return: org.springframework.web.servlet.ModelAndView
     **/
    @GetMapping("detail")
    public ModelAndView detail(HttpSession session,String strOrderNo){


        Map map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }

        ServerResponse orderVo = orderService.detail(user.getId(), NumberUtil.stringToLong(strOrderNo));

        map.put("result",orderVo);
        return new ModelAndView("portal/order/orderDetail",map);
    }
    /**
     * 描 述: 分页查询订单列表
     * @date: 2019-04-26 16:15
     * @author: lhpang
     * @param: [session, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @GetMapping("list")
    public ModelAndView list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                  @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        Map map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }
        ServerResponse orderList = orderService.list(user.getId(),pageNum,pageSize);

        map.put("result",orderList);

        return new ModelAndView("portal/order/orderList",map);
    }


    /**
     * 描 述: 支付接口
     * @date: 2019-04-26 10:43
     * @author: lhpang
     * @param: [session, orderNo, request]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping("pay")
    public ModelAndView pay(HttpSession session, String strOrderNo, HttpServletRequest request){
        Map map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        ServerResponse img = orderService.pay(NumberUtil.stringToLong(strOrderNo),user.getId(),path);
        map.put("result", img);
        return new ModelAndView("portal/order/scan",map);
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
    @PostMapping(value = "queryPayStatus")
    public ServerResponse<Boolean> queryPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.queryOrderPayStatus(user.getId(), orderNo).isSuccess() ? ServerResponse.createBySuccess(true) : ServerResponse.createBySuccess(false);
    }
    
}
