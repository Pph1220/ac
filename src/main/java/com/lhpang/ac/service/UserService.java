package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;

/**
*   类路径: com.lhpang.ac.service.UserService
*   描述:  UserService
*   @author: lhpang
*   @date: 2019-04-17 10:13
*/
public interface UserService {
    /**
     * 描 述: 登陆Service
     * @date: 2019/4/17 20:44
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<User> login(String logno, String password);
    /**
     * 描 述: 用户注册
     * @date: 2019/4/17 21:47
     * @author: lhpang
     * @param: 
     * @return: 
    **/
    ServerResponse<String> register(User user);
    /**
     * 描 述: 检查账号,电话是否唯一
     * @date: 2019/4/17 22:05
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<String> checkValid(String s,String type);
    /**
     * 描 述: 忘记密码
     * @date: 2019/4/17 22:20
     * @author: lhpang
     * @param: 
     * @return: 
    **/
    ServerResponse<String> forgetPassword(String Password);
    /**
     * 描 述: 获得忘记密码问题
     * @date: 2019-04-18 10:17
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<String> getQuestion(String logNo);
    /**
     * 描 述: 验证密保答案
     * @date: 2019-04-18 10:31
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<String> checkAnswer(String logNo,String question,String answer);
}
