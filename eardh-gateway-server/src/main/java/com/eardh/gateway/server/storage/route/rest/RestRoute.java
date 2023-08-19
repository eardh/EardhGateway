package com.eardh.gateway.server.storage.route.rest;

import lombok.Data;

/**
 * rest路由表
 */
@Data
public class RestRoute {

    /**
     * api标识
     */
    private String apiId;

    /**
     * 微服务名称
     */
    private String microserviceName;

    /** 微服务分组名称 */
    private String microserviceGroup;

    /**
     * url地址
     */
    private String restUrl;

    /**
     * rest方法
     */
    private String restMethod;

}