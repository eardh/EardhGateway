package com.eardh.gateway.center.model.rpc.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用接口方法
 * @author eardh
 * @date 2023/3/22 13:31
 */
@Data
@Schema(description = "应用接口方法")
public class DubboMethodVO extends BaseModel {

    /** 方法标识 */
    @Schema(description = "方法标识")
    @EqualsAndHashCode.Exclude
    private String methodId;

    /** 网关API标识 */
    @Schema(description = "网关API标识")
    private String apiId;

    /** 微服务标识 */
    @Schema(description = "微服务标识")
    private String microserviceId;

    /** 接口标识 */
    @Schema(description = "接口标识")
    private String interfaceId;

    /** 方法名称 */
    @Schema(description = "方法名称")
    private String methodName;

    /** 方法描述 */
    @Schema(description = "方法描述")
    @EqualsAndHashCode.Exclude
    private String methodDescription;

    /**
     * 方法参数类型
     */
    @Schema(description = "方法参数类型")
    private String methodParametersType;

    /**
     * 方法参数名
     */
    @Schema(description = "方法参数名")
    private String methodParametersName;

    /** 方法返回值类型 */
    @Schema(description = "方法返回值类型")
    private String methodResultType;
}