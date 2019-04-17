package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.protal.UserController
*   描述: 用户Controller
*   @author: lhpang
*   @date: 2019-04-17 14:01
*/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 描 述: 登陆
     * @date: 2019-04-17 14:05
     * @author: lhpang
     * @param: [name, password, session]
     * @return: java.lang.Object
     **/
    @ResponseBody
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public ServerResponse<User> login(String logno, String password, HttpSession session){

        ServerResponse<User> login = userService.login(logno, password);

        if(login.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER, login.getData());
        }

        return login;
    }
    
}
