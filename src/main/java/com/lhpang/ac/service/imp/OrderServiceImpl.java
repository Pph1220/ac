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
import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.config.AlipayConfig;
import com.lhpang.ac.dao.OrderItemMapper;
import com.lhpang.ac.dao.OrderMapper;
import com.lhpang.ac.dao.PayInfoMapper;
import com.lhpang.ac.pojo.Order;
import com.lhpang.ac.pojo.OrderItem;
import com.lhpang.ac.pojo.PayInfo;
import com.lhpang.ac.service.OrderService;
import com.lhpang.ac.utils.BigDecimalUtil;
import com.lhpang.ac.utils.DateUtil;
import com.lhpang.ac.utils.ImgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().setAlipayPublicKey(alipayConfig.getAlipayPublicKey()).setAppid(alipayConfig.getAppid()).setCharset(alipayConfig.getCharset()).setFormat(alipayConfig.getFormat()).setGatewayUrl(alipayConfig.getOpenApiDomain()).setPrivateKey(alipayConfig.getPrivateKey()).setSignType(alipayConfig.getSignType()).build();

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("阿C外卖扫码支付").toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("共").append(totalAmount).toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "阿C本人";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "10m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItems = orderItemMapper.selectByOrderNoUserId(orderNo, userId);

        for (OrderItem orderItem:orderItems){
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getId().toString(),orderItem.getProductName(), BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(),Double.valueOf(100)).longValue(),orderItem.getQuantity());
            goodsDetailList.add(goodsDetail);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(alipayConfig.getNotifyUrl())//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
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

                // 需要修改为运行机器上的路径
                String filePath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String fileName = String.format("qr-%s.png",response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);


                resultMap.put("Img", ImgUtil.ImageToBase64(filePath));
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
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
            boolean aliPayRSACheckedV2 = AlipaySignature.rsaCheckV2(map, alipayConfig.getAlipayPublicKey(), "utf-8",alipayConfig.getSignType());
            if(!aliPayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证失败",e);
        }

        Long orderNo = Long.valueOf(map.get("out-trade_no"));
        String tradeNo = map.get("trade_no");
        String tradeStatus = map.get("trade_status");

        Order order = orderMapper.selectByOrderNo(orderNo);

        if(order == null){
            log.warn("订单不存在,回调忽略");
            return ServerResponse.createByErrorMessage("订单不存在,回调忽略");
        }

        if(order.getStatus() >= Constant.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }

        if(Constant.AlipayCallback.RESPONSE_SUCCESS.equals(tradeStatus)){
            order.setStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setCreateTime(DateUtil.strToDate(map.get("gmy_payment")));
            orderMapper.updateByPrimaryKeySelective(order);
        }

        PayInfo payInfo = new PayInfo();

        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Constant.PaymentTypeEnum.ONLINE_PAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));

        payInfoMapper.insert(payInfo);
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

}
