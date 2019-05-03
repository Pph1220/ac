package com.lhpang.ac.controller.protal;

import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Shipping;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.ShippingService;
import com.lhpang.ac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
*   类路径: com.lhpang.ac.controller.protal.ShippingController
*   描述: 收货地址Controller
*   @author: lhpang
*   @date: 2019-04-24 09:52
*/
@Slf4j
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;
    @Autowired
    private UserService userService;

    /**
     * 描 述: 新增收货地址
     * @date: 2019-04-24 10:16
     * @author: lhpang
     * @param: [session, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping("add")
    public ServerResponse add(HttpSession session, Shipping shipping){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }
        return shippingService.add(user.getId(),shipping);
    }
    /**
     * 描 述: 删除收货地址
     * @date: 2019-04-24 10:26
     * @author: lhpang
     * @param: [session, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping("delete")
    public ServerResponse delete (HttpSession session,Integer shippingId){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return shippingService.delete(user.getId(), shippingId);
    }
    /**
     * 描 述: 更新收货地址
     * @date: 2019-04-24 10:44
     * @author: lhpang
     * @param: [session, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "update",method = RequestMethod.GET)
    public ServerResponse update(HttpSession session, Shipping shipping){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return shippingService.update(user.getId(),shipping);
    }
    /**
     * 描 述: 收货地址详情
     * @date: 2019-04-24 10:44
     * @author: lhpang
     * @param: [session, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public ServerResponse<Shipping> detail(HttpSession session, Integer shippingId){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return shippingService.detail(user.getId(),shippingId);
    }
    /**
     * 描 述: 查询收货地址列表
     * @date: 2019-04-24 11:01
     * @author: lhpang
     * @param: [pageNum, pageSize, session]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @GetMapping("list")
    public ModelAndView list(HttpSession session){
        Map map = Maps.newHashMap();

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }
        ServerResponse list = shippingService.list(user.getId());
        map.put("result", list);
        if(!list.isSuccess()){
            return new ModelAndView("common/fail",map);
        }

        return new ModelAndView("portal/shipping/shippingList",map);
    }

}
