package com.eardh.gateway.center.model.aggregator;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * rest路由表
 */
@Schema(description ="rest路由表")
@Data
public class RestRoute extends BaseModel {

    /**
     * Rest接口标识
     */
    @Schema(description = "Rest接口标识")
    private String id;

    /**
     * api标识
     */
    @Schema(description = "api标识")
    private String apiId;

    /**
     * 微服务标识
     */
    @Schema(description = "微服务标识")
    private String microserviceId;

    /**
     * 微服务名称
     */
    @Schema(description = "微服务名称")
    private String microserviceName;

    /** 微服务分组名称 */
    @Schema(description = "微服务分组名称")
    private String microserviceGroup;

    /** 微服务描述 */
    @Schema(description = "微服务描述")
    private String microserviceDescription;

    /**
     * url地址
     */
    @Schema(description = "url地址")
    private String restUrl;

    /**
     * rest方法
     */
    @Schema(description = "rest方法")
    private String restMethod;

    /**
     * 参数类型
     */
    @Schema(description = "参数类型")
    private String restParametersType;

    /**
     * 参数名
     */
    @Schema(description = "参数名")
    private String restParametersName;

    /** 返回值类型 */
    @Schema(description = "返回值类型")
    private String restResultType;


    /**
     * rest描述信息
     */
    @Schema(description = "rest描述信息")
    private String restDescription;

}