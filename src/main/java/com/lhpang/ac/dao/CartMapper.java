package com.lhpang.ac.dao;

import com.lhpang.ac.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    /**
     * 描 述: 查找这个商品在不在这个用户的购物车里
     * @date: 2019-04-23 14:34
     * @author: lhpang
     * @param:  Integer userId,Integer productId
     * @return: Cart
     **/
    Cart selectByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    /**
     * 描 述: 通过userId查找购物车
     * @date: 2019-04-23 14:39
     * @author: lhpang
     * @param: Integer userId
     * @return: List<Cart>
    **/
    List<Cart> selectCartByUserId(Integer userId);
    /**
     * 描 述: 根据UserId查找购物车中未被选中的数量
     * @date: 2019-04-23 16:56
     * @author: lhpang
     * @param:  Integer userId
     * @return: int
    **/
    int selectCartProductCheckedStatusByUserId(Integer userId);
    /**
     * 描 述: //TODO 
     * @date: 2019/4/23 22:26
     * @author: lhpang
     * @param: Integer userId,Integer productId
     * @return: int
    **/
    int deleteCartByUserIdProductId(@Param("userId")Integer userId,@Param("productId")String productId);
    /**
     * 描 述: //TODO
     * @date: 2019/4/23 23:24
     * @author: lhpang
     * @param: Integer userId,Integer checked
     * @return: int
    **/
    int checkOrUnCheckProduct(@Param("userId")Integer userId,@Param("checked")Integer checked,
                              @Param("productId") Integer productId);
    /**
     * 描 述: 查询购物车中商品总数
     * @date: 2019/4/23 23:46
     * @author: lhpang
     * @param: Integer userId
     * @return: int
    **/
    int getCartProductCount(Integer userId);
    /**
     * 描 述: 通过用户购物车中已勾选的商品
     * @date: 2019-04-26 10:49
     * @author: lhpang
     * @param:
     * @return:
    **/
    List<Cart> selectCheckedByUserId(Integer userId);
}