package com.eardh.gateway.center.model.rpc.vo;

import com.eardh.gateway.center.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 应用接口
 * @author eardh
 * @date 2023/3/22 13:29
 */
@Data
@Schema(description = "应用接口")
public class DubboInterfaceVO extends BaseModel {

    /** 接口标识 */
    @Schema(description = "interface_id")
    private String interfaceId;

    /** 微服务标识 */
    @Schema(description = "微服务标识")
    private String microserviceId;

    /** 接口名称 */
    @Schema(description = "接口名称")
    private String interfaceName;

    /** 接口描述 */
    @Schema(description = "接口描述")
    private String interfaceDescription;

    /** 接口所属组 */
    @Schema(description = "接口所属组")
    private String interfaceGroup;

}

