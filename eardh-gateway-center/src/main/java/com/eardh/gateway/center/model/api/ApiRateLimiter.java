package com.eardh.gateway.center.model.api;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API限流表
 */
@TableName(value ="api_rate_limiter")
@Data
public class ApiRateLimiter extends BaseModel {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @EqualsAndHashCode.Exclude
    private Integer id;

    /**
     * API标识
     */
    @TableField(value = "api_id", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long apiId;

    /**
     * 限流规则 - 每秒生产的令牌速度，单位秒
     */
    @TableField(value = "limiter_rule")
    private String limiterRule;

    /**
     * 令牌桶容量 - 令牌满了停止生产
     */
    @TableField(value = "bucket_capacity")
    private Integer bucketCapacity;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    @TableField(value = "forbidden")
    private byte forbidden;
}