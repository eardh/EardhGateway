package com.eardh.gateway.center.model.api.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * API调用日志
 * @TableName api_invoke_log
 */
@Schema(description ="API调用日志")
@Data
public class ApiInvokeLogVO extends BaseModel {

    /**
     * API标识
     */
    @Schema(description = "API标识")
    private String apiId;

    /**
     * 调用者IP地址
     */
    @Schema(description = "调用者IP地址")
    private String invokerIp;

    /**
     * 调用耗时，单位毫秒
     */
    @Schema(description = "调用耗时，单位毫秒")
    private Integer rpcInvokeTime;

    /**
     * 调用者用户信息
     */
    @Schema(description = "调用者用户信息")
    private String invokerUserInfo;
}