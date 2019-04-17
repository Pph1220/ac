package com.lhpang.ac.service.Imp;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.UserMapper;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
*   类路径: com.lhpang.ac.service.Imp.UserService
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-17 10:16
*/
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String logno, String password) {

        int count = userMapper.checkLogno(logno);

        if(count == 0){
            return ServerResponse.createByErrorMessage("登陆账号不存在");
        }

        User user = userMapper.login(logno, password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码有误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功",user);
    }
}
