package com.lhpang.ac.controller.backend;

import com.github.pagehelper.PageInfo;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.backend.OrderManageOrder
*   描述: 管理员订单Controller
*   @author: lhpang
*   @date: 2019-04-26 16:16
*/
@Controller
@RequestMapping("/manager/order/")
public class OrderManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 描 述: 查询订单列表
     * @date: 2019-04-26 16:30
     * @author: lhpang
     * @param: [session, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){

        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.managerList(pageNum, pageSize);

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

        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.managerDetail(orderNo);
    }
    /**
     * 描 述: 搜索订单
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "search",method = RequestMethod.GET)
    public ServerResponse search(HttpSession session,Long orderNo){

        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return orderService.search(orderNo);
    }
    /**
     * 描 述: 发货
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "send",method = RequestMethod.GET)
    public ServerResponse send(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return  orderService.send(orderNo);

    }
    
}
