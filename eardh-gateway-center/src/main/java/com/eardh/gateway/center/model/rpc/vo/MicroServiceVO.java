package com.eardh.gateway.center.model.rpc.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微服务VO
 * @author eardh
 * @date 2023/3/22 11:16
 */
@Data
@Schema(description = "微服务")
public class MicroServiceVO extends BaseModel {

    /** 微服务标识 */
    @Schema(description = "微服务标识")
    private String microserviceId;

    /** 微服务名称 */
    @Schema(description = "微服务名称")
    private String microserviceName;

    @Schema(description = "微服务描述")
    private String microserviceDescription;

    /** 微服务分组名称 */
    @Schema(description = "微服务分组名称")
    private String microserviceGroup;
}
