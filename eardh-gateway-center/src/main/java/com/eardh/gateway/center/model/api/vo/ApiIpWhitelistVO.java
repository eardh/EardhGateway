package com.eardh.gateway.center.model.api.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * API白名单列表
 */
@Schema(description ="API白名单列表")
@Data
public class ApiIpWhitelistVO extends BaseModel {

    /**
     * API标识
     */
    @Schema(description = "api_id")
    private String apiId;

    /**
     * 白名单列表
     */
    @Schema(description = "白名单列表")
    private String ipWhitelist;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    @Schema(description = "是否禁止，0 - 不禁止；1 - 禁止")
    private byte forbidden;
}