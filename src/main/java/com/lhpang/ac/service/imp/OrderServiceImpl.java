package com.lhpang.ac.service.imp;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.config.AlipayConfig;
import com.lhpang.ac.dao.*;
import com.lhpang.ac.pojo.*;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.utils.BigDecimalUtil;
import com.lhpang.ac.utils.DateUtil;
import com.lhpang.ac.utils.ImgUtil;
import com.lhpang.ac.vo.OrderItemVo;
import com.lhpang.ac.vo.OrderVo;
import com.lhpang.ac.vo.ShippingVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.net.www.protocol.http.ntlm.NTLMAuthentication;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.lhpang.ac.common.ServerResponse.createByErrorMessage;

/**
*   类路径: com.lhpang.ac.service.imp.OrderService
*   描述: 订单ServiceImpl
*   @author: lhpang
*   @date: 2019-04-17 10:19
*/
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 描 述: 购买
     * @date: 2019-04-26 11:55
     * @author: lhpang
     * @param: [userId, shippingId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    @Transactional
    public synchronized ServerResponse create(Integer userId, Integer shippingId) {

        if (shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        //从购物车中获取商品
        List<Cart> carts = cartMapper.selectCartByUserId(userId);

        ServerResponse response = this.getCartOrderItem(userId, carts);
        if (!response.isSuccess()) {
            return response;
        }

        List<OrderItem> orderItems = (List<OrderItem>) response.getData();

        if(orderItems.size() == 0){
            return ServerResponse.createByErrorMessage("购物车位空");
        }

        //计算总价
        BigDecimal orderTotalPrice = this.getOrderTotalPrice(orderItems);
        //生成订单
        Order order = this.toOrder(userId, shippingId, orderTotalPrice);

        if(order == null){
            return ServerResponse.createByErrorMessage("生成订单错误");
        }
        //为OrderItem赋值orderNo
        for(OrderItem orderItem : orderItems){
            orderItem.setOrderNo(order.getOrderNo());
        }

        //Mybatis批量插入OrderItems
        int count = orderItemMapper.batchInsert(orderItems);

        //减少库存
        this.removeProductStock(orderItems);
        //清空购物车
        this.cleanCart(carts);

        OrderVo orderVo = this.assembleOrderVo(order, orderItems);

        return ServerResponse.createBySuccess(orderVo);
    }
    /**
     * 描 述: 取消订单
     * @date: 2019-04-26 15:31
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    public ServerResponse<String> cancle(Integer userId,Long orderNo){

        if(orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("该用户不存在此订单");
        }

        if(order.getStatus() != Constant.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("已付款,无法取消订单");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Constant.OrderStatusEnum.CANCELED.getCode());

        int count = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("取消成功");
        }

        return ServerResponse.createByErrorMessage("取消失败");
    }
    /**
     * 描 述: 订单详情
     * @date: 2019-04-26 15:54
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse detail(Integer userId,Long orderNo){
        if(orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);

        if(order == null){
            return ServerResponse.createByErrorMessage("该用户不存在此订单");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoUserId(orderNo, userId);

        OrderVo orderVo = assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);

    }
    /**
     * 描 述: 分页查询订单列表
     * @date: 2019-04-26 16:14
     * @author: lhpang
     * @param: [userId, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){

        PageHelper.startPage(pageNum,pageSize);

        List<Order> orderList = orderMapper.selectByUserId(userId);

        List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
        PageInfo resultPageInfo = new PageInfo<>(orderList);
        resultPageInfo.setList(orderVoList);

        return ServerResponse.createBySuccess(resultPageInfo);
    }
    /**
     * 描 述: 组装OrderVoList
     * @date: 2019-04-26 16:11
     * @author: lhpang
     * @param: [orderList, userId]
     * @return: java.util.List<com.lhpang.ac.vo.OrderVo>
     **/
    private List<OrderVo> assembleOrderVoList(List<Order> orderList,Integer userId){

        List<OrderVo> orderVoList = Lists.newArrayList();

        for(Order order : orderList){
            List<OrderItem> orderItemList = Lists.newArrayList();
            if(userId == null){
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            }else{
                orderItemList = orderItemMapper.selectByOrderNoUserId(order.getOrderNo(), userId);
            }
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
    /**
     * 描 述: 组装OrderVo
     * @date: 2019-04-26 14:18
     * @author: lhpang
     * @param: [order, orderItems]
     * @return: com.lhpang.ac.vo.OrderVo
     **/
    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItems){

        OrderVo orderVo = new OrderVo();

        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Constant.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Constant.OrderStatusEnum.codeOf(order.getStatus()).getValue());
        orderVo.setPaymentTime(DateUtil.dateToString(order.getPaymentTime()));
        orderVo.setDendTime(DateUtil.dateToString(order.getDendTime()));
        orderVo.setEndTime(DateUtil.dateToString(order.getEndTime()));
        orderVo.setCreateTime(DateUtil.dateToString(order.getCreateTime()));
        orderVo.setUpdateTime(DateUtil.dateToString(order.getUpdateTime()));
        List<OrderItemVo> orderItemVos = Lists.newArrayList();
        for(OrderItem orderItem : orderItems){
            orderItemVos.add(assembleOrderItem(orderItem));
        }
        orderVo.setOrderItemVos(orderItemVos);
        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShipping(shipping));
        }

        return orderVo;
    }
    /**
     * 描 述: 组装OrderItemVo
     * @date: 2019-04-26 14:31
     * @author: lhpang
     * @param: [orderItem]
     * @return: com.lhpang.ac.vo.OrderItemVo
     **/
    private OrderItemVo assembleOrderItem(OrderItem orderItem){

        OrderItemVo orderItemVo = new OrderItemVo();

        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateUtil.dateToString(orderItem.getCreateTime()));
        orderItemVo.setUpdateTime(DateUtil.dateToString(orderItem.getUpdateTime()));

        return orderItemVo;

    }
    /**
     * 描 述: 组装ShippingVo
     * @date: 2019-04-26 14:15
     * @author: lhpang
     * @param: [shipping]
     * @return: com.lhpang.ac.vo.ShippingVo
     **/
    private ShippingVo assembleShipping(Shipping shipping){

        ShippingVo shippingVo = new ShippingVo();

        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());

        return shippingVo;
    }
    /**
     * 描 述: 清空购物车
     * @date: 2019-04-26 13:58
     * @author: lhpang
     * @param: [carts]
     * @return: void
     **/
    private void cleanCart(List<Cart> carts){

        for(Cart cart : carts){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }
    /**
     * 描 述: 通过orderItem减少Product的库存
     * @date: 2019-04-26 13:40
     * @author: lhpang
     * @param: [orderItems]
     * @return: void
     **/
    private void removeProductStock(List<OrderItem> orderItems){
        for(OrderItem orderItem : orderItems){
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());

            productMapper.updateByPrimaryKeySelective(product);
        }
    }
    /**
     * 描 述: 创建订单
     * @date: 2019-04-26 11:28
     * @author: lhpang
     * @param: [userId, shippingId, orderTotalPrice]
     * @return: com.lhpang.ac.pojo.Order
     **/
    private synchronized Order toOrder(Integer userId,Integer shippingId,BigDecimal orderTotalPrice){

        Order order = new Order();
        //订单号
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        //订单状态
        order.setStatus(Constant.OrderStatusEnum.NO_PAY.getCode());
        //订单支付方式
        order.setPaymentType(Constant.PaymentTypeEnum.ONLINE_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        //用户id
        order.setUserId(userId);
        //收货地址id
        order.setShippingId(shippingId);
        //创建时间
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));

        int count = orderMapper.insert(order);
        if(count > 0 ){
            return order;
        }
        return null;
    }
    /**
     * 描 述: 生成订单号
     * @date: 2019-04-26 11:22
     * @author: lhpang
     * @param: []
     * @return: long
     **/
    private synchronized long generateOrderNo(){

        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }


    /**
     * 描 述: 计算订单总价
     * @date: 2019-04-26 11:17
     * @author: lhpang
     * @param: [orderItems]
     * @return: java.math.BigDecimal
     **/
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){

        BigDecimal bigDecimal = new BigDecimal("0");

        for(OrderItem orderItem : orderItems){
            bigDecimal = BigDecimalUtil.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }

    /**
     * 描 述: 通过购物车List和userId构建List<OrderItem>
     *     OrderItem-cart  1-1
     * @date: 2019-04-26 11:11
     * @author: lhpang
     * @param: [userId, carts]
     * @return: com.lhpang.ac.common.ServerResponse<java.util.List<com.lhpang.ac.pojo.OrderItem>>
     **/
    private ServerResponse getCartOrderItem(Integer userId,List<Cart> carts){

        List<OrderItem> result = Lists.newArrayList();
        if(carts.size() == 0){
            return ServerResponse.createByErrorMessage("购物车中没有选中的商品");
        }
        for(Cart cart : carts){
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product.getStatus() != Constant.ProductStatusEnum.ON_SALE.getCode()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在售状态");
            }

            //校验库存
            if(cart.getQuantity() > product.getStock()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue()));
            orderItem.setCreateTime(new Timestamp(System.currentTimeMillis()));

            result.add(orderItem);
        }

        return ServerResponse.createBySuccess(result);
    }

    /**
     * 描 述: 核心支付方法
     * @date: 2019-04-25 15:58
     * @author: lhpang
     * @param: [orderNo, userId, path]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse pay(Long orderNo,Integer userId,String path){

        Map<String,String> resultMap = Maps.newHashMap();

        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null ){
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        AlipayTradeService tradeService =
                new AlipayTradeServiceImpl.ClientBuilder().setAlipayPublicKey(alipayConfig.getAlipayPublicKey()).setAppid(alipayConfig.getAppid()).setCharset(alipayConfig.getCharset()).setFormat(alipayConfig.getFormat()).setGatewayUrl(alipayConfig.getOpenApiDomain()).setPrivateKey(alipayConfig.getPrivateKey()).setSignType(alipayConfig.getSignType()).build();

        String outTradeNo = String.valueOf(order.getOrderNo());

        String subject = new StringBuilder().append("阿C外卖扫码支付").toString();

        String totalAmount = order.getPayment().toString();

        String undiscountableAmount = "0";

        String sellerId = "";

        String body = new StringBuilder().append("订单").append(outTradeNo).append("共").append(totalAmount).toString();

        String operatorId = "阿C本人";

        String storeId = "test_store_id";

        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        String timeoutExpress = "10m";

        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItems = orderItemMapper.selectByOrderNoUserId(orderNo, userId);

        for (OrderItem orderItem:orderItems){
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getId().toString(),orderItem.getProductName(), BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),Double.valueOf(100)).longValue(),orderItem.getQuantity());
            goodsDetailList.add(goodsDetail);
        }

        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(alipayConfig.getNotifyUrl())
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File file = new File(path);
                if(!file.exists()){
                    file.setWritable(true);
                    file.mkdirs();
                }

                String filePath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String fileName = String.format("qr-%s.png",response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                resultMap.put("Img", ImgUtil.ImageToBase64(filePath));
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return createByErrorMessage("支付宝预下单失败!!!");
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return createByErrorMessage("系统异常，预下单状态未知!!!");
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }
    /**
     * 描 述: 阿里回调方法
     * @date: 2019-04-25 16:52
     * @author: lhpang
     * @param: [map]
     * @return: java.lang.Object
     **/
    @Override
    public ServerResponse aliPayCallBack(Map requestMap){

        Map<String,String> map = Maps.newHashMap();
        log.info(requestMap.toString());

        for (Iterator iterator = requestMap.keySet().iterator();iterator.hasNext();){
            String name = (String) iterator.next();
            String [] values = (String []) requestMap.get(name);
            String valueStr = "";
            for (int i = 0;i < values.length;i++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name,valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}",map.get("sign"),map.get("trade_status"),map.toString());
        //验证签名
        map.remove("sign_type");
        try {
            boolean aliPayRSACheckedV2 = AlipaySignature.rsaCheckV2(map, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(),alipayConfig.getSignType());
            if(!aliPayRSACheckedV2){
                return createByErrorMessage("非法请求");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证失败",e);
        }

        Long orderNo = Long.valueOf(map.get("out_trade_no"));
        String tradeNo = map.get("trade_no");
        String tradeStatus = map.get("trade_status");

        Order order = orderMapper.selectByOrderNo(orderNo);

        if(order == null){
            log.warn("订单不存在,回调忽略");
            return createByErrorMessage("订单不存在,回调忽略");
        }

        if(order.getStatus() >= Constant.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }

        if(Constant.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPaymentTime(DateUtil.strToDate(map.get("gmt_payment")));
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();
        //支付宝两次回调,先通过orderNo查询payinfo如果没有插入
        PayInfo info = payInfoMapper.selectByOrderNo(orderNo);
        if(info == null){
            payInfo.setUserId(order.getUserId());
            payInfo.setOrderNo(order.getOrderNo());
            payInfo.setPayPlatform(Constant.PaymentTypeEnum.ONLINE_PAY.getCode());
            payInfo.setPlatformNumber(tradeNo);
            payInfo.setPlatformStatus(tradeStatus);
            payInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

            payInfoMapper.insert(payInfo);

            return ServerResponse.createBySuccess();
        }

        //如果有修改状态
        payInfoMapper.updateStatusByOrderNo(orderNo,tradeStatus);

        return ServerResponse.createBySuccess();
    }
    /**
     * 描 述: 查询订单支付状态
     * @date: 2019-04-26 10:12
     * @author: lhpang
     * @param: [userId, orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse queryOrderPayStatus(Integer userId,Long orderNo){

        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);

        if(order == null ){
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        if(order.getStatus() >= Constant.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();


    }
    /**
     * 描 述: 打印支付宝返回的Response
     * @date: 2019-04-25 15:57
     * @author: lhpang
     * @param: [response]
     * @return: void
     **/
    private void dumpResponse(AlipayResponse response) {

        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    /**
     * 描 述: 后台订单列表
     * @date: 2019-04-26 16:29
     * @author: lhpang
     * @param: [pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    public ServerResponse<PageInfo> managerList(int pageNum,int pageSize){

        PageHelper.startPage(pageNum, pageSize);

        List<Order> orderList = orderMapper.selectPayedOrder();

        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);

        PageInfo resultPageInfo = new PageInfo(orderList);
        resultPageInfo.setList(orderVoList);

        return ServerResponse.createBySuccess(resultPageInfo);
    }
    /**
     * 描 述:后台订单详情
     * @date: 2019-04-26 16:36
     * @author: lhpang
     * @param: [orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse managerDetail(Long orderNo){

        if(orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Order order = orderMapper.selectByOrderNo(orderNo);

        if(order == null){
            return ServerResponse.createByErrorMessage("订单不存在");
        }

        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);

        OrderVo orderVo = assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);

    }
    /**
     * 描 述: 后台通过orderNo搜索
     * @date: 2019-04-26 17:08
     * @author: lhpang
     * @param: [orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse search(Long orderNo){

        if(orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Order order = orderMapper.selectByOrderNo(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);

        OrderVo orderVo = assembleOrderVo(order,orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }
    /**
     * 描 述: 后台发货
     * @date: 2019-04-28 9:30
     * @author: lhpang
     * @param: [orderNo]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse send(Long orderNo){
        if(orderNo == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Order order = orderMapper.selectByOrderNo(orderNo);

        if(order == null){
            return ServerResponse.createByErrorMessage("订单不存在");
        }
        if(order.getStatus() > Constant.OrderStatusEnum.SHIPPED.getCode()){
            return ServerResponse.createByErrorMessage("请勿重复发货");
        }
        order.setStatus(Constant.OrderStatusEnum.SHIPPED.getCode());
        order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        orderMapper.updateByPrimaryKeySelective(order);
        return ServerResponse.createBySuccessMessage("发货成功");
    }
}
