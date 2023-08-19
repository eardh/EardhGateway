package com.eardh.gateway.center.model.rpc.rest;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * rest协议接口信息
 */
@TableName(value ="rest_interface")
@Data
public class RestInterface extends BaseModel {

    /**
     * rest标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @EqualsAndHashCode.Exclude
    private Long id;

    /**
     * api标识
     */
    @TableField(value = "api_id")
    private Long apiId;

    /**
     * 微服务标识
     */
    @TableField(value = "microservice_id", insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Long microserviceId;

    /**
     * url地址
     */
    @TableField(value = "rest_url", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String restUrl;

    /**
     * rest方法类型
     */
    @TableField(value = "rest_method", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String restMethod;

    /**
     * 参数类型
     */
    @TableField(value = "rest_parameters_type")
    private String restParametersType;

    /**
     * 参数名
     */
    @TableField("rest_parameters_name")
    private String restParametersName;

    /** 返回值类型 */
    @TableField("rest_result_type")
    private String restResultType;

    /**
     * rest描述信息
     */
    @TableField(value = "rest_description")
    private String restDescription;

}