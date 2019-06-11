package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;

/**
 * 类路径: com.lhpang.ac.service.UserService
 * 描述:  UserService
 *
 * @author: lhpang
 * @date: 2019-04-17 10:13
 */
public interface UserService {
    /**
     * 描 述: 登陆Service
     *
     * @date: 2019/4/17 20:44
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<User> login(String logno, String password);

    /**
     * 描 述: 用户注册
     *
     * @date: 2019/4/17 21:47
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> register(User user);

    /**
     * 描 述: 检查账号,电话是否唯一
     *
     * @date: 2019/4/17 22:05
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> checkValid(String s, String type);

    /**
     * 描 述: 获得忘记密码问题
     *
     * @date: 2019-04-18 10:17
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> getQuestion(String logNo);

    /**
     * 描 述: 验证密保答案
     *
     * @date: 2019-04-18 10:31
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> checkAnswer(String logNo, String answer);

    /**
     * 描 述: 忘记密码
     *
     * @date: 2019/4/18 22:53
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> forgetPassword(String logNo, String newPassword, String answer);

    /**
     * 描 述: 登陆状态下修改密码
     *
     * @date: 2019/4/18 22:53
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<String> updatePasswordOnLine(User user, String oldPassword, String newPassword);

    /**
     * 描 述: 更新个人信息
     *
     * @date: 2019/4/18 23:11
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<User> updateInformation(User currentUser, User updateUser);

    /**
     * 描 述: 获得当前用户信息
     *
     * @date: 2019/4/18 23:18
     * @author: lhpang
     * @param:
     * @return:
     **/
    ServerResponse<User> getInformation(Integer userId);

    /**
     * 描 述: 判断当前用户是否为管理员
     *
     * @date: 2019/4/18 23:21
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> checkAdminRole(User user);

    /**
     * 描 述: 判断是否在登陆状态
     *
     * @date: 2019-04-19 16:56
     * @author: lhpang
     * @param: [user]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> checkOnLine(User user);

    /**
     * 描 述: 验证用户是否为管理员和是否在线
     *
     * @date: 2019-04-22 15:25
     * @author: lhpang
     * @param: [user]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> checkRoleAndOnLine(User user);
}
