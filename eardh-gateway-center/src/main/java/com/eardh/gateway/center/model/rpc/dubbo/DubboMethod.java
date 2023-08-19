package com.eardh.gateway.center.model.rpc.dubbo;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Dubbo协议方法信息
 * @author eardh
 * @date 2023/3/22 13:31
 */
@Data
@TableName("dubbo_method")
public class DubboMethod extends BaseModel {

    /** 方法标识 */
    @TableId(value = "method_id", type = IdType.ASSIGN_ID)
    @EqualsAndHashCode.Exclude
    private Long methodId;

    /** 网关API标识 */
    @TableField(value = "api_id")
    private Long apiId;

    /** 微服务标识 */
    @TableField(value = "microservice_id", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long microserviceId;

    /** Dubbo接口类标识 */
    @TableField(value = "interface_id", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long interfaceId;

    /** 方法名称 */
    @TableField(value = "method_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String methodName;

    /** 方法描述 */
    @TableField("method_description")
    @EqualsAndHashCode.Exclude
    private String methodDescription;

    /**
     * 方法参数类型
     */
    @TableField(value = "method_parameters_type")
    private String methodParametersType;

    /**
     * 方法参数名
     */
    @TableField("method_parameters_name")
    private String methodParametersName;

    /** 方法返回值类型 */
    @TableField("method_result_type")
    private String methodResultType;

}