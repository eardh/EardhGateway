package com.eardh.gateway.sdk.config;

import cn.hutool.core.net.NetUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eardh
 * @date 2023/3/31 16:18
 */
@ConfigurationProperties(prefix = "eardh-gateway.microservice")
@Data
public class MicroserviceProperties {

    /** 微服务名称 */
    private String name;

    /** 微服务描述 */
    private String description;

    /** 微服务分组名称 */
    private String group;

    /**
     * api服务、rpc服务、注册中心通用配置
     */
    private Config config = new Config();

    @Data
    public static class Config {

        /**
         * 微服务路径
         */
        private String microservicePath = "";

        /** 网关中心 ip:port */
        private String gatewayRegistry;

        /**
         * 服务暴露的端口号
         */
        private Integer port = NetUtil.getUsableLocalPort(50000);

        /**
         * 微服务协议类型，如 dubbo、rest、tri等
         */
        private Protocol protocol =  Protocol.tri;

        /**
         * 微服务版本
         */
        private String version = "1.0.0";

        /**
         * 服务权重
         */
        private double weight = 1.0;

        /**
         * 序列化算法
         */
        private String serialization = "fastjson2";
    }

    /**
     * 是否启动rpc服务
     */
    private Boolean rpcServiceEnable = false;

    /**
     * 是否启用微服务注册中心
     */
    private Boolean registryCenterEnable = false;

    /**
     * 是否上报微服务信息
     */
    private Boolean reportEnable = false;

    public static enum Protocol {

        dubbo,tri,http,https;

    }
}
