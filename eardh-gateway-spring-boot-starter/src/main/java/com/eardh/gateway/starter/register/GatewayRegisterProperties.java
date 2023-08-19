package com.eardh.gateway.starter.register;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eardh
 * @date 2023/3/30 15:12
 */
@ConfigurationProperties(prefix = "eardh-gateway.gateway.register")
@Data
public class GatewayRegisterProperties {

    /** 是否注册网关服务 */
    private Boolean enable = false;

    /** 分组名称 */
    private String groupName;

    /** 网关名称 */
    private String gatewayName;

    /** 网关描述 */
    private String gatewayDescription;

    /** 注册中心地址 ip:port，用于上报网关服务 */
    private String gatewayRegistry;

    /**
     * 是否注册网关到Nacos注册中心
     */
    private Boolean enableRegistry = false;

}
