package com.lhpang.ac.service.Imp;

import com.lhpang.ac.dao.UserMapper;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.IUserService;
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
    public User select(Integer id) {

        return userMapper.selectByPrimaryKey(id);
    }
}
