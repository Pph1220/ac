package com.lhpang.ac.controller.protal;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
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
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public ServerResponse<User> login(String logNo, String password, HttpSession session){

        ServerResponse<User> login = userService.login(logNo, password);

        if(login.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER, login.getData());
        }

        return login;
    }
    /**
     * 描 述: 登出
     * @date: 2019/4/17 20:41
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public ServerResponse<String> logout (HttpSession session){

        session.removeAttribute(Constant.CURRENT_USER);

        return ServerResponse.createBySuccess();
    }
    /**
     * 描 述: 用户注册
     * @date: 2019/4/17 21:49
     * @author: lhpang
     * @param:  User
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public ServerResponse<String> register(User user){

        return userService.register(user);
    }
    /**
     * 描 述: 检查登陆账号,电话是否唯一
     * @date: 2019/4/17 22:07
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "checkValid",method = RequestMethod.POST)
    public ServerResponse<String> checkValid(String s,String type){

        return userService.checkValid(s,type);
    }
    /**
     * 描 述: 获取当前用户信息
     * @date: 2019/4/17 22:10
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @ResponseBody
    @RequestMapping(value = "getUserInfo",method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpSession session){

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("未登录");
    }
    /**
     * 描 述: 获得密保问题
     * @date: 2019-04-18 11:40
     * @author: lhpang
     * @param: [logNo]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "getQuestion",method = RequestMethod.POST)
    public ServerResponse<String> getQuestion(String logNo){
        
        return userService.getQuestion(logNo);
    }
    /**
     * 描 述: 检查密保问题答案
     * @date: 2019-04-18 11:42
     * @author: lhpang
     * @param: [logNo, question, answer]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "checkAnswer",method = RequestMethod.POST)
    public ServerResponse<String> checkAnswer(String logNo,String answer){
        
        return userService.checkAnswer(logNo, answer);
    }
    /**
     * 描 述: 忘记密码
     * @date: 2019/4/18 22:50
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "forgetPassword",method = RequestMethod.POST)
    public ServerResponse<String> forgetPassword(String logNo,String newPassword,String answer){
        return userService.forgetPassword(logNo,newPassword,answer);
    }
    /**
     * 描 述: 登陆状态忘记密码
     * @date: 2019/4/18 22:50
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "updatePasswordOnLine",method = RequestMethod.POST)
    public ServerResponse<String> updatePasswordOnLine(HttpSession session,String oldPassword,String newPassword){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        return userService.updatePasswordOnLine(user, oldPassword, newPassword);
    }
    /**
     * 描 述: 更新用户信息
     * @date: 2019/4/18 22:52
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @ResponseBody
    @RequestMapping(value = "updateInformation",method = RequestMethod.POST)
    public ServerResponse<User> updateInformation(User user,HttpSession session){

        User currentUser = (User) session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        ServerResponse<User> response = userService.updateInformation(currentUser,user);

        if(response.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER, response.getData());
        }
        return response;
    }
    /**
     * 描 述: 获得当前用户信息
     * @date: 2019/4/18 23:24
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @ResponseBody
    @RequestMapping(value = "getInformation",method = RequestMethod.POST)
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return userService.getInformation(currentUser.getId());

    }
}
