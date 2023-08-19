package com.eardh.gateway.center.model.api;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;

/**
 * 网关API
 * @author eardh
 * @date 2023/4/3 10:42
 */
@Data
@TableName("gateway_api")
public class GatewayApi extends BaseModel {

    /**
     * api标识
     */
    @TableId(value = "api_id", type = IdType.ASSIGN_ID)
    private Long apiId;

    /**
     * api描述
     */
    @TableField(value = "api_description")
    private String apiDescription;

    /**
     * api路径
     */
    @TableField(value = "api_path", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String apiPath;

    /**
     * api方法，如http的get、post等
     */
    @TableField(value = "api_method", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String apiMethod;

    /**
     * api协议，rest、dubbo、tri等rpc协议
     */
    @TableField(value = "api_protocol", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String apiProtocol;

    /**
     * API是否认证
     */
    @TableField(value = "api_auth")
    private Integer apiAuth;

}
