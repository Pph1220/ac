package com.lhpang.ac.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.ShippingMapper;
import com.lhpang.ac.pojo.Shipping;
import com.lhpang.ac.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
*   类路径: com.lhpang.ac.service.imp.ShippingService
*   描述: 收货地址ServiceImpl
*   @author: lhpang
*   @date: 2019-04-17 10:17
*/
@Service("ShippingService")
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 描 述: 新增收货地址
     * @date: 2019-04-24 10:14
     * @author: lhpang
     * @param: [userId, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse add(Integer userId, Shipping shipping){

        if (shipping == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        shipping.setUserId(userId);

        int count = shippingMapper.insert(shipping);

        if(count > 0){
            Map<String,Integer> map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新增地址成功",map);
        }
        return ServerResponse.createByErrorMessage("新增地址失败");
    }
    /**
     * 描 述: 删除收货地址
     * @date: 2019-04-24 10:24
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse delete(Integer userId,Integer shippingId){

        if (shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int count = shippingMapper.deleteByShippingIdAndUserId(shippingId,userId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }
    /**
     * 描 述: 更新收货地址
     * @date: 2019-04-24 10:41
     * @author: lhpang
     * @param: [userId, shipping]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse update(Integer userId,Shipping shipping){

        if (shipping == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        shipping.setUserId(userId);

        int count = shippingMapper.updateByShippingUserId(shipping);
        if(count > 0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }
    /**
     * 描 述: 查询收货地址详情
     * @date: 2019-04-24 10:52
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.Shipping>
     **/
    @Override
    public ServerResponse<Shipping> detail(Integer userId,Integer shippingId){

        if (shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId,userId);

        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }

        return ServerResponse.createBySuccess(shipping);
    }
    /**
     * 描 述: 查询收货地址列表
     * @date: 2019-04-24 11:00
     * @author: lhpang
     * @param: [userId]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo<com.lhpang.ac.pojo.Shipping>>
     **/
    @Override
    public ServerResponse list(Integer userId){
        List<Shipping> shippingList  = shippingMapper.selectByUserId(userId);

        return ServerResponse.createBySuccess(shippingList);
    }
}
