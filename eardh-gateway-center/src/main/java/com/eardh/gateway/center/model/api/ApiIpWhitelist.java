package com.eardh.gateway.center.model.api;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API白名单列表
 */
@TableName(value ="api_ip_whitelist")
@Data
public class ApiIpWhitelist extends BaseModel {

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
     * 白名单列表
     */
    @TableField(value = "ip_whitelist")
    private String ipWhitelist;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    @TableField(value = "forbidden")
    private byte forbidden;
}