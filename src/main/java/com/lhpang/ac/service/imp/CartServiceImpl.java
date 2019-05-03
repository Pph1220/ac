package com.lhpang.ac.service.imp;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.CartMapper;
import com.lhpang.ac.dao.ProductMapper;
import com.lhpang.ac.pojo.Cart;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.service.CartService;
import com.lhpang.ac.utils.BigDecimalUtil;
import com.lhpang.ac.vo.CartProductVo;
import com.lhpang.ac.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
*   类路径: com.lhpang.ac.service.imp.CartService
*   描述: 购物车ServiceImpl
*   @author: lhpang
*   @date: 2019-04-17 10:20
*/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 描 述: 加入购物车
     * @date: 2019-04-23 14:38
     * @author: lhpang
     * @param: [userId, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse<CartVo> add(Integer userId,Integer count ,Integer productId){
        //判断参数
        if(count == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);

        if(cart == null){
            //这个产品不在这个购物车里面,需要新增
            Cart newCart = new Cart();
            newCart.setQuantity(count);
            newCart.setProductId(productId);
            newCart.setUserId(userId);
            newCart.setChecked(Constant.Cart.CHECKED);

            int insertResult = cartMapper.insert(newCart);

            if(insertResult <= 0){
                return ServerResponse.createByErrorMessage("加入购物车失败");
            }
        }else{
            //产品已经在购物车里面
            count = count + cart.getQuantity();
            cart.setQuantity(count);
            int updateResult = cartMapper.updateByPrimaryKeySelective(cart);

            if(updateResult <= 0){
                return ServerResponse.createByErrorMessage("加入购物车失败");
            }
        }
        return this.list(userId);
    }
    /**
     * 描 述: 修改购物车中商品
     * @date: 2019/4/23 21:55
     * @author: lhpang
     * @param: [userId, count, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer count, Integer productId) {
        //判断参数
        if(count == null || productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectByUserIdProductId(userId, productId);

        Product product = productMapper.selectByPrimaryKey(productId);

        if(product.getStock() < count){
            return ServerResponse.createByErrorMessage("库存不足");
        }
        if (cart != null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return ServerResponse.createBySuccess();
    }
    /**
     * 描 述: 删除购物车中商品
     * @date: 2019/4/23 22:02
     * @author: lhpang
     * @param: [userId, productIds]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {

        Iterable<String> iterable = Splitter.on(",").split(productIds);

        if(!iterable.iterator().hasNext()){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Iterator<String> iterator = iterable.iterator();
        while (iterator.hasNext()){
            if(iterator.next() != null){
                cartMapper.deleteCartByUserIdProductId(userId, iterator.next());
            }
        }

        return this.list(userId);
    }
    /**
     * 描 述: 查询购物车中的商品
     * @date: 2019/4/23 22:38
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @Override
    public ServerResponse<CartVo> list(Integer userId) {

        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }
    /**
     * 描 述: 全选,全不选,选择,不选择
     * @date: 2019/4/23 23:26
     * @author: lhpang
     * @param: [userId, productId, checked]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.CartVo>
     **/
    @Override
    public ServerResponse<CartVo> checkOrUnCheckProduct(Integer userId,Integer productId,Integer checked) {
        cartMapper.checkOrUnCheckProduct(userId, checked,productId);

        return this.list(userId);
    }
    /**
     * 描 述: 查询购物车中商品总数
     * @date: 2019/4/23 23:50
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Integer>
     **/
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId){

        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.getCartProductCount(userId));
    }

    /**
     * 描 述: 根据userId获得CartVo
     * @date: 2019-04-23 17:05
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.vo.CartVo
     **/
    private CartVo getCartVoLimit(Integer userId){

        CartVo cartVo = new CartVo();

        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVos = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(!CollectionUtils.isEmpty(carts)){
            for(Cart cart : carts){
                CartProductVo cartProductVo = new CartProductVo();

                cartProductVo.setId(cart.getId());
                cartProductVo.setUserId(cart.getUserId());
                cartProductVo.setProductId(cart.getProductId());

                Product product = productMapper.selectByPrimaryKey(cart.getProductId());

                if(product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cart.getQuantity()){
                        buyLimitCount = cart.getQuantity();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Constant.Cart.LIMIT_NUM_FAIL);
                        //更新有效库存
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cart1);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity().doubleValue()));
                    cartProductVo.setProductChecked(cart.getChecked());
                }
                //计算购物车总价
                if(cart.getChecked() == Constant.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVos.add(cartProductVo);
            }

            cartVo.setCartProductVoList(cartProductVos);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setAllChecked(this.getAllCheckStatus(userId));
        }

        return cartVo;

    }
    /**
     * 描 述: 判断购物车中商品是否全选
     * @date: 2019-04-23 17:00
     * @author: lhpang
     * @param: [userId]
     * @return: boolean
     **/
    private boolean getAllCheckStatus(Integer userId){

        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
    
}
