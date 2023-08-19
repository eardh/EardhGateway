package com.eardh.gateway.server.invocation.rest;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;

/**
 * @author eardh
 * @date 2023/4/14 16:35
 */
@Data
public class RestCallStatement {

    /**
     * 调用的服务主机ip
     */
    private String ip;

    /**
     * 调用的服务主机端口
     */
    private Integer port;

    /**
     * 请求资源地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 协议类型
     */
    private String protocol;

    public RestCallStatement(Instance instance) {
        this.ip = instance.getIp();
        this.port = instance.getPort();
        this.protocol = instance.getMetadata().getOrDefault("protocol", "http");
    }
}
