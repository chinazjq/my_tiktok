package com.zjq.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信配置
 */
@Component
@Data
@PropertySource("classpath:tencentcloud.properties")
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {

    // 认证用户id
    private String secretId;

    // 认证用户密钥
    private String secretKey;

    // 域名
    private String endpoint;

    // appid
    private String smsAppId;

    // 签名
    private String signName;

    // 使用模板id
    private String templateId;

}
