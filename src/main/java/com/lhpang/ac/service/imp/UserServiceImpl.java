package com.lhpang.ac.service.imp;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.UserMapper;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.utils.MD5Util;
import com.lhpang.ac.utils.Util;
import org.apache.catalina.Server;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
*   类路径: com.lhpang.ac.service.imp.UserService
*   描述:  UserService
*   @author: lhpang
*   @date: 2019-04-17 10:16
*/
@Service
public class UserServiceImpl implements com.lhpang.ac.service.UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 描 述: 登陆
     * @date: 2019/4/17 23:11
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.User>
     **/
    @Override
    public ServerResponse<User> login(String logNo, String password) {

        int count = userMapper.checkLogNo(logNo);

        if(count == 0){
            return ServerResponse.createByErrorMessage("登陆账号不存在");
        }
        //对比的是加密后的密码
        String MD5password = MD5Util.md5(password);
        System.out.println(MD5password);

        User user = userMapper.login(logNo, MD5password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码有误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功",user);
    }

    /**
     * 描 述: 注册
     * @date: 2019/4/17 20:46
     * @author: lhpang
     * @param:
     * @return:
    **/
    @Override
    public ServerResponse<String> register(User user){
        //检查登陆账号
        ServerResponse<String> response = this.checkValid(user.getLogno(), Constant.LOGNO);
        if(!response.isSuccess()){
            return response;
        }
        //检查电话是否唯一
        response = this.checkValid(user.getLogno(), Constant.PHONE);
        if(!response.isSuccess()){
            return response;
        }
        //注册为用户级别
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        //密码加密
        user.setPassword(MD5Util.md5(user.getPassword()));
        //添加
        int resultCount = userMapper.insert(user);
        //判断是否添加成功
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess("注册成功");
    }
    /**
     * 描 述: 检查
     * @date: 2019/4/17 21:56
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> checkValid(String s, String type){

        int count = 0;
        if (StringUtils.isBlank(type)){
            if(Constant.LOGNO.equals(type)){
                count = userMapper.checkLogNo(s);

                if(count > 0){
                    return ServerResponse.createByErrorMessage("登陆账号已存在");
                }
            }
            if(Constant.PHONE.equals(type)){
                count = userMapper.checkPhone(s);
                if(count > 0){
                    return ServerResponse.createByErrorMessage("注册电话已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> forgetPassword(String Password) {

        return null;
    }
}
