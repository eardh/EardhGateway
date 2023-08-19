package com.eardh.gateway.center.model.api.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author eardh
 * @date 2023/4/3 11:31
 */
@Data
@Schema(description = "网关API")
public class GatewayApiVO extends BaseModel {

    /**
     * api标识
     */
    @Schema(description = "api标识")
    @EqualsAndHashCode.Exclude
    private String apiId;

    /**
     * api描述
     */
    @Schema(description = "api描述")
    private String apiDescription;

    /**
     * api路径
     */
    @Schema(description = "api路径")
    private String apiPath;

    /**
     * api方法，如http的get、post等
     */
    @Schema(description = "api方法")
    private String apiMethod;

    /**
     * api类型，rest、dubbo、tri等rpc协议
     */
    @Schema(description = "协议")
    private String apiProtocol;

    /**
     * API是否认证
     */
    @Schema(description = "是否认证")
    private Integer apiAuth;
}
