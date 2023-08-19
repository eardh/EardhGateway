package com.eardh.gateway.center.model.aggregator;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author eardh
 * @date 2023/3/26 20:43
 */
@Data
@Schema(description = "dubbo路由信息")
public class DubboRoute extends BaseModel {

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
     * 接口标识
     */
    @Schema(description = "接口标识")
    private String interfaceId;

    /**
     * 接口名称，类名
     */
    @Schema(description = "接口名称")
    private String interfaceName;

    /** 接口所属组 */
    @Schema(description = "接口所属组")
    private String interfaceGroup;

    /** 接口描述 */
    @Schema(description = "接口描述")
    private String interfaceDescription;

    /**
     * 方法标识
     */
    @Schema(description = "方法标识")
    private String methodId;

    /**
     * 方法名
     */
    @Schema(description = "方法名")
    private String methodName;

    /** 方法描述 */
    @Schema(description = "方法描述")
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

    /**
     * 	返回值类型
     */
    @Schema(description = "返回值类型")
    private String methodResultType;

}
