package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShippingMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdAndUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    int updateByShippingUserId(Shipping shipping);

    Shipping selectByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    List<Shipping> selectByUserId(Integer userId);
}