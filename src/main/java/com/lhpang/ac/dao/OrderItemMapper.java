package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> selectByOrderNoUserId(@Param("orderNo") Long orderNo,@Param("userId") Integer userId);

    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);

    List<OrderItem> selectByOrderNo(@Param("orderNo") Long orderNo);
}