package com.eardh.gateway.center.model.api;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;

/**
 * API调用日志
 * @TableName api_invoke_log
 */
@TableName(value ="api_invoke_log")
@Data
public class ApiInvokeLog extends BaseModel {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * API标识
     */
    @TableField(value = "api_id", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long apiId;

    /**
     * 调用者IP地址
     */
    @TableField(value = "invoker_ip")
    private String invokerIp;

    /**
     * 调用耗时，单位毫秒
     */
    @TableField(value = "rpc_invoke_time")
    private Integer rpcInvokeTime;

    /**
     * 调用者用户信息
     */
    @TableField(value = "invoker_user_info")
    private String invokerUserInfo;
}