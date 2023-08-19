package com.eardh.gateway.center.model.rpc.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * rest协议接口信息
 */
@Schema(description ="rest协议接口信息")
@Data
public class RestInterfaceVO extends BaseModel {

    /**
     * rest标识
     */
    @Schema(description = "rest路由标识")
    @EqualsAndHashCode.Exclude
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
     * url地址
     */
    @Schema(description = "url地址")
    private String restUrl;

    /**
     * rest方法类型，get/post/···
     */
    @Schema(description = "rest方法类型")
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