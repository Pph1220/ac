package com.lhpang.ac.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
*   类路径: com.lhpang.ac.config.alipayConfig
*   描述: 支付宝参数配置类
*   @author: lhpang
*   @date: 2019-04-25 09:50
*/
@Slf4j
@ConfigurationProperties(prefix = "alipay")
@Data
@Component
public class AlipayConfig {

    private String openApiDomain;

    private String mcloudApiDomain;

    private String pid;

    private String appid;

    private String privateKey;

    private String publicKey;

    private String alipayPublicKey;

    private String signType;

    private int maxQueryRetry;

    private long queryDuration;

    private int maxCancelRetry;

    private long cancelDuration;

    private long heartbeatDelay;

    private long heartbeatDuration;

    private String notifyUrl;

    private String  charset = "utf-8";

    private String format = "json";

    @PostConstruct
    public void init() {
        log.info(this.description());
    }

    public String description() {
        StringBuilder sb = new StringBuilder("\nConfigs{\n");
        sb.append("支付宝openapi网关: ").append(openApiDomain).append(",").append("\n");
        if (StringUtils.isNotEmpty(mcloudApiDomain)) {
            sb.append("支付宝mcloudapi网关域名: ").append(mcloudApiDomain).append(",").append("\n");
        }

        if (StringUtils.isNotEmpty(pid)) {
            sb.append("pid: ").append(pid).append(",").append("\n");
        }
        sb.append("appid: ").append(appid).append(",").append("\n");

        sb.append("商户RSA私钥: ").append(getKeyDescription(privateKey)).append(",").append("\n");
        sb.append("商户RSA公钥: ").append(getKeyDescription(publicKey)).append(",").append("\n");
        sb.append("支付宝RSA公钥: ").append(getKeyDescription(alipayPublicKey)).append(",").append("\n");
        sb.append("签名类型: ").append(signType).append(",").append("\n");

        sb.append("查询重试次数: ").append(maxQueryRetry).append(",").append("\n");
        sb.append("查询间隔(毫秒): ").append(queryDuration).append(",").append("\n");
        sb.append("撤销尝试次数: ").append(maxCancelRetry).append(",").append("\n");
        sb.append("撤销重试间隔(毫秒): ").append(cancelDuration).append(",").append("\n");

        sb.append("交易保障调度延迟(秒): ").append(heartbeatDelay).append(",").append("\n");
        sb.append("交易保障调度间隔(秒): ").append(heartbeatDuration).append(",").append("\n");
        sb.append("回调地址: ").append(notifyUrl).append(",").append("\n");
        sb.append("编码集: ").append(charset).append(",").append("\n");
        sb.append("形式: ").append(format);
        sb.append("}");

        return sb.toString();
    }

    private static String getKeyDescription(String key) {
        int showLength = 6;
        if (StringUtils.isNotEmpty(key) &&
                key.length() > showLength) {
            return new StringBuilder(key.substring(0, showLength))
                    .append("******")
                    .append(key.substring(key.length() - showLength))
                    .toString();
        }
        return null;
    }
    
}
