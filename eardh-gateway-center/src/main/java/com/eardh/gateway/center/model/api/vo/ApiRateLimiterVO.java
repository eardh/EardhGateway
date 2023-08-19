package com.eardh.gateway.center.model.api.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * API限流表
 */
@Schema(description ="API限流表")
@Data
public class ApiRateLimiterVO extends BaseModel {

    /**
     * API标识
     */
    @Schema(description = "API标识")
    private String apiId;

    /**
     * 限流规则 - 每秒生产的令牌速度，单位秒
     */
    @Schema(description = "限流规则")
    private String limiterRule;

    /**
     * 令牌桶容量 - 令牌满了停止生产
     */
    @Schema(description = "令牌桶容量")
    private Integer bucketCapacity;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    @Schema(description = "是否禁止")
    private byte forbidden;
}