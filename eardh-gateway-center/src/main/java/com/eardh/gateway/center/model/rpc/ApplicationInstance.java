package com.eardh.gateway.center.model.rpc;

import lombok.Data;

/**
 * 应用实例
 * @author eardh
 * @date 2023/4/17 13:58
 */
@Data
public class ApplicationInstance {

    /**
     * IP地址
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 权重
     */
    private Double weight;

    /**
     * 健康状态
     */
    private Boolean healthy;

    /**
     * 实例可以接受请求
     */
    private Boolean enable;

    /**
     * 版本
     */
    private String version;


}
