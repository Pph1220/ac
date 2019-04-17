package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;

/**
*   类路径: com.lhpang.ac.service.UserService
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-17 10:13
*/
public interface UserService {

    ServerResponse<User> login(String logno, String password);

}
