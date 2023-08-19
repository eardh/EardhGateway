package com.eardh.gateway.center.model.api.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * API黑名单列表
 */
@Schema(description ="API黑名单列表")
@Data
public class ApiIpBlacklistVO extends BaseModel {

    /**
     * API标识
     */
    @Schema(description = "API标识")
    private String apiId;

    /**
     * 黑名单列表
     */
    @Schema(description = "黑名单列表")
    private String ipBlacklist;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    @Schema(description = "是否禁止，0 - 不禁止；1 - 禁止")
    private byte forbidden;

}