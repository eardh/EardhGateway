package com.eardh.gateway.center.model.gateway.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 网关信息VO
 * @author eardh
 * @date 2023/3/22 13:44
 */
@Data
@Schema(description = "网关服务")
public class GatewayServerVO extends BaseModel {

    /** 网关标识 */
    @Schema(description = "网关标识")
    private String gatewayId;

    /** 网关名称 */
    @Schema(description = "网关名称")
    private String gatewayName;

    /** 网关描述 */
    @Schema(description = "网关名称")
    private String gatewayDescription;

    /** 分组标识 */
    @Schema(description = "分组名称")
    private String groupName;

}

