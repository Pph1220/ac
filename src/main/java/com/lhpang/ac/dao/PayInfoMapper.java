package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.PayInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}