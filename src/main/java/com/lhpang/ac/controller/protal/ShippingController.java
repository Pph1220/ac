package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Shipping;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.ShippingService;
import com.lhpang.ac.service.UserService;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.protal.ShippingController
*   描述: 收货地址Controller
*   @author: lhpang
*   @date: 2019-04-24 09:52
*/
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
    @RequestMapping(value = "add",method = RequestMethod.GET)
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
    @RequestMapping(value = "delete",method = RequestMethod.GET)
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
    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ServerResponse list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,HttpSession session){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return shippingService.list(user.getId(), pageNum, pageSize);
    }

}
