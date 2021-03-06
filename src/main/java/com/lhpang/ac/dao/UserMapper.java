package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 类路径: com.lhpang.ac.dao
 * 描述: 用户Mapper
 *
 * @author: lhpang
 * @date: 2019-04-17 14:19
 */
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 描 述: 检查登陆账号是否存在
     *
     * @date: 2019-04-17 15:50
     * @author: lhpang
     * @param: logno
     * @return: int
     **/
    int checkLogNo(String logNo);

    /**
     * 描 述: 登陆
     *
     * @date: 2019-04-17 15:51
     * @author: lhpang
     * @param: logno, password
     * @return: User
     **/
    User login(@Param("logNo") String logNo, @Param("password") String password);

    /**
     * 描 述: 检查电话是否存在
     *
     * @date: 2019/4/17 20:48
     * @author: lhpang
     * @param: String phone
     * @return:
     **/
    int checkPhone(String phone);

    /**
     * 描 述: 获得忘记密码问题
     *
     * @date: 2019-04-18 10:28
     * @author: lhpang
     * @param: logNo
     * @return:
     **/
    String getQuestion(String logNo);

    int checkAnswer(@Param("logNo") String logNo, @Param("answer") String answer);

    int forgetPassword(@Param("logNo") String logNo, @Param("newPassword") String newPassword);

    int checkPhoneByUserId(@Param("id") Integer id, @Param("phone") String phone);
}