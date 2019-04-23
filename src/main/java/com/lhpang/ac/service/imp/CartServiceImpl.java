package com.lhpang.ac.service.imp;

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
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
        CartVo cartVo = this.getCartVoLimit(userId);

        return ServerResponse.createBySuccess(cartVo);
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

                Product product = productMapper.selectByPrimaryKey(cart.getId());

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
