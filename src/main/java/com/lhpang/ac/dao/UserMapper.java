package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *   类路径: com.lhpang.ac.dao
 *   描述: 用户Mapper
 *   @author: lhpang
 *   @date: 2019-04-17 14:19
 */
@Repository
@Mapper
public interface UserMapper {
    
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    /**
     * 描 述: 检查登陆账号是否存在
     * @date: 2019-04-17 15:50
     * @author: lhpang
     * @param: logno
     * @return: int
    **/
    int checkLogno(String logno);
    /**
     * 描 述: 登陆
     * @date: 2019-04-17 15:51
     * @author: lhpang
     * @param: logno,password
     * @return: User
    **/
    User login(@Param("logno") String logno, @Param("password") String password);
}