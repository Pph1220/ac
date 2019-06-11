package com.lhpang.ac.service.imp;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.UserMapper;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 类路径: com.lhpang.ac.service.imp.UserService
 * 描述:  UserService
 *
 * @author: lhpang
 * @date: 2019-04-17 10:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 描 述: 登陆
     *
     * @date: 2019/4/17 23:11
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @Override
    public ServerResponse<User> login(String logNo, String password) {

        int count = userMapper.checkLogNo(logNo);

        if (count == 0) {
            return ServerResponse.createByErrorMessage("登陆账号不存在");
        }
        //对比的是加密后的密码
        String MD5password = MD5Util.md5(password);

        User user = userMapper.login(logNo, MD5password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码有误");
        }
        user.setPassword(null);

        return ServerResponse.createBySuccess("登陆成功", user);
    }

    /**
     * 描 述: 注册
     *
     * @date: 2019/4/17 20:46
     * @author: lhpang
     * @param:
     * @return:
     **/
    @Override
    public ServerResponse<String> register(User user) {
        //检查登陆账号
        ServerResponse<String> response = this.checkValid(user.getLogno(), Constant.LOGNO);
        if (!response.isSuccess()) {
            return response;
        }
        //检查电话是否唯一
        response = this.checkValid(user.getPhone(), Constant.PHONE);
        if (!response.isSuccess()) {
            return response;
        }
        //注册为用户级别
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        //密码加密
        user.setPassword(MD5Util.md5(user.getPassword()));
        //创建时间
        user.setCreatTime(new Timestamp(System.currentTimeMillis()));
        //添加
        int resultCount = userMapper.insert(user);
        //判断是否添加成功
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess("注册成功");
    }

    /**
     * 描 述: 检查
     *
     * @date: 2019/4/17 21:56
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkValid(String s, String type) {

        int count = 0;
        if (StringUtils.isNotBlank(type)) {
            if (Constant.LOGNO.equals(type)) {
                count = userMapper.checkLogNo(s);

                if (count > 0) {
                    return ServerResponse.createByErrorMessage("登陆账号已存在");
                }
            }
            if (Constant.PHONE.equals(type)) {
                count = userMapper.checkPhone(s);
                if (count > 0) {
                    return ServerResponse.createByErrorMessage("注册电话已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 描 述: 获得密保问题
     *
     * @date: 2019-04-18 11:06
     * @author: lhpang
     * @param: [logNo]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> getQuestion(String logNo) {
        //检查登陆账号
        ServerResponse<String> response = this.checkValid(logNo, Constant.LOGNO);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("账号不存在");
        }
        String question = userMapper.getQuestion(logNo);

        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("问题为空");
    }

    /**
     * 描 述: 检查密保答案
     *
     * @date: 2019-04-18 11:06
     * @author: lhpang
     * @param: [answer]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkAnswer(String logNo, String answer) {

        if (StringUtils.isBlank(logNo)) {
            return ServerResponse.createByErrorMessage("登陆账号为空");
        } else {
            if (this.checkValid(logNo, Constant.LOGNO).isSuccess()) {
                return ServerResponse.createByErrorMessage("账号不存在");
            }
            if (StringUtils.isBlank(answer)) {
                return ServerResponse.createByErrorMessage("答案为空");
            }
            if (userMapper.checkAnswer(logNo, answer) > 0) {
                return ServerResponse.createBySuccess();
            }
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }

    /**
     * 描 述: 忘记密码
     *
     * @date: 2019/4/18 23:11
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> forgetPassword(String logNo, String newPassword, String answer) {

        //检查登陆账号
        ServerResponse<String> response = this.checkValid(logNo, Constant.LOGNO);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        if (StringUtils.isBlank(answer)) {
            return ServerResponse.createByErrorMessage("答案为空");
        }
        ServerResponse<String> checkAnswer = this.checkAnswer(logNo, answer);
        if (!checkAnswer.isSuccess()) {
            return checkAnswer;
        }
        if (StringUtils.isBlank(newPassword)) {
            return ServerResponse.createByErrorMessage("新密码为空");
        }
        String MD5newPassword = MD5Util.md5(newPassword);

        int count = userMapper.forgetPassword(logNo, MD5newPassword);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 描 述: 登陆状态修改密码
     *
     * @date: 2019/4/18 23:11
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> updatePasswordOnLine(User user, String oldPassword, String newPassword) {
        //判断旧密码
        User login = userMapper.login(user.getLogno(), MD5Util.md5(oldPassword));
        if (login == null) {
            return ServerResponse.createByErrorMessage("原密码错误");
        }
        if (StringUtils.isBlank(newPassword)) {
            return ServerResponse.createByErrorMessage("请输入新密码");
        }
        user.setPassword(MD5Util.md5(newPassword));
        //更新密码
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 描 述: 更新个人信息
     *
     * @date: 2019/4/18 23:07
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @Override
    public ServerResponse<User> updateInformation(User currentUser, User updateUser) {

        int count = userMapper.checkPhoneByUserId(currentUser.getId(), updateUser.getPhone());
        if (count > 0) {
            return ServerResponse.createByErrorMessage("此电话已存在");
        }

        updateUser.setId(currentUser.getId());
        //对比要修改的属性与原有属性的值,如果不一样修改
        if (!StringUtils.equals(currentUser.getPhone(), updateUser.getPhone()) && StringUtils.isNotBlank(updateUser.getPhone())) {
            currentUser.setPhone(updateUser.getPhone());
        }
        if (!StringUtils.equals(currentUser.getName(), updateUser.getName()) && StringUtils.isNotBlank(updateUser.getName())) {
            currentUser.setName(updateUser.getName());
        }
        if (!StringUtils.equals(currentUser.getQuestion(), updateUser.getQuestion()) && StringUtils.isNotBlank(updateUser.getQuestion())) {
            currentUser.setQuestion(updateUser.getQuestion());
        }
        if (!StringUtils.equals(currentUser.getAnswer(), updateUser.getAnswer()) && StringUtils.isNotBlank(updateUser.getAnswer())) {
            currentUser.setAnswer(updateUser.getAnswer());
        }

        currentUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        int resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (resultCount > 0) {
            //更新成功将修改后的User返回
            return ServerResponse.createBySuccess("更新成功", currentUser);
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    /**
     * 描 述: 获得当前用户
     *
     * @date: 2019/4/18 23:19
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @Override
    public ServerResponse<User> getInformation(Integer userId) {

        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 描 述: 判断当前用户是否为管理员
     *
     * @date: 2019/4/18 23:21
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Constant.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("无权限操作");
    }

    /**
     * 描 述: 判断是否在登陆状态
     *
     * @date: 2019-04-19 16:56
     * @author: lhpang
     * @param: [user]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkOnLine(User user) {
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return ServerResponse.createBySuccess();
    }

    /**
     * 描 述: 验证用户是否为管理员和是否在线
     *
     * @date: 2019-04-22 15:25
     * @author: lhpang
     * @param: [user]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkRoleAndOnLine(User user) {
        //判断是否为登陆状态
        ServerResponse onLine = this.checkOnLine(user);
        if (!onLine.isSuccess()) {
            return onLine;
        }
        //判断是否为管理员
        ServerResponse adminRole = this.checkAdminRole(user);
        if (!adminRole.isSuccess()) {
            return adminRole;
        }
        return ServerResponse.createBySuccess();
    }
}
