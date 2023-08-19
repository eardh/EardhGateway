package com.eardh.gateway.server.storage.ratelimiter;

import lombok.Data;

/**
 * API限流表
 */
@Data
public class ApiRateLimiter {

    /**
     * API标识
     */
    private String apiId;

    /**
     * 限流规则 - 每秒生产的令牌速度，单位秒
     */
    private Double limiterRule;

    /**
     * 令牌桶容量 - 令牌满了停止生产
     */
    private Integer bucketCapacity;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    private Boolean forbidden;
}