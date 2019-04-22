package com.lhpang.ac.service.imp;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.ProductMapper;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.service.ProductService;
import oracle.jrockit.jfr.openmbean.ProducerDescriptorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
*   类路径: com.lhpang.ac.service.imp.ProductService
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-17 10:18
*/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 描 述: 商品新增或修改
     * @date: 2019-04-22 16:36
     * @author: lhpang
     * @param: [product]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public synchronized ServerResponse<String> saveOrUpdateProduct(Product product) {

        if(product != null){
            if(product.getId() != null){
                int count = productMapper.updateByPrimaryKeySelective(product);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("更新成功");
                }
                return ServerResponse.createByErrorMessage("更新失败");
            }else{
                product.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int count = productMapper.insertSelective(product);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("新增成功");
                }
                return ServerResponse.createByErrorMessage("新增失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }
    /**
     * 描 述: 修改上下架状态
     * @date: 2019-04-22 16:43
     * @author: lhpang
     * @param: [id]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public synchronized ServerResponse<String> setStatus(Integer productId) {

        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //判断是否有这个产品
        Product product = productMapper.selectByPrimaryKey(productId);
        Product updateProduct = new Product();
        updateProduct.setId(productId);

        if(product == null){
            return ServerResponse.createByErrorMessage("商品不存在");
        }else{
            //如果为在下架改为在售
            if(product.getStatus() == Constant.ProductStatusEnum.OFF_SALE.getCode()){
                updateProduct.setStatus(Constant.ProductStatusEnum.ON_SALE.getCode());
                int count = productMapper.updateByPrimaryKeySelective(updateProduct);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("上架成功");
                }
            }
            //如果为在售改为下架
            if(product.getStatus() == Constant.ProductStatusEnum.ON_SALE.getCode()){
                updateProduct.setStatus(Constant.ProductStatusEnum.OFF_SALE.getCode());
                int count = productMapper.updateByPrimaryKeySelective(updateProduct);
                if(count > 0){
                    return ServerResponse.createBySuccessMessage("下架成功");
                }
            }
        }

        return ServerResponse.createByErrorMessage("修改失败");
    }
    /**
     * 描 述: 商品详情
     * @date: 2019-04-22 17:53
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Object>
     **/
    public ServerResponse<Object> managerProductDetail(Integer productId){

        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);

        if(product == null){
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }
        return null;
    }
}
