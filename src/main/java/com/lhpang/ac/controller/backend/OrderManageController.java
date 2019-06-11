package com.lhpang.ac.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 类路径: com.lhpang.ac.controller.backend.OrderManageOrder
 * 描述: 管理员订单Controller
 *
 * @author: lhpang
 * @date: 2019-04-26 16:16
 */
@Controller
@RequestMapping("/orderManager/")
public class OrderManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 描 述: 查询订单列表
     *
     * @date: 2019-04-26 16:30
     * @author: lhpang
     * @param: [session, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @ResponseBody
    @GetMapping("list")
    public ModelAndView list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map map = Maps.newHashMap();

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            map.put("result", response);
            return new ModelAndView("common/fail", map);
        }
        ServerResponse list = orderService.managerList(pageNum, pageSize);
        map.put("result", list);

        return new ModelAndView("backend/order/orderList", map);

    }

    /**
     * 描 述: 订单详情
     *
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @GetMapping("detail")
    public ModelAndView detail(HttpSession session, String strOrderNo) {
        Map map = Maps.newHashMap();

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            map.put("result", response);
            return new ModelAndView("common/fail", map);
        }
        ServerResponse detail = orderService.managerDetail(NumberUtil.stringToLong(strOrderNo));
        map.put("result", detail);

        return new ModelAndView("backend/order/orderDetail", map);
    }

    /**
     * 描 述: 搜索订单
     *
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ServerResponse search(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return orderService.search(orderNo);
    }

    /**
     * 描 述: 发货
     *
     * @date: 2019-04-26 15:56
     * @author: lhpang
     * @param: [session, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping(value = "send")
    public ServerResponse send(HttpSession session, String strOrderNo) {
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return orderService.send(NumberUtil.stringToLong(strOrderNo));

    }

}
