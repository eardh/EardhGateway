package com.eardh.gateway.center.model.gateway;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;

/**
 * 网关信息
 * @author eardh
 * @date 2023/3/22 13:44
 */
@Data
@TableName("gateway_server")
public class GatewayServer extends BaseModel {

    /** 分组标识 */
    @TableId(value = "gateway_id", type = IdType.ASSIGN_ID)
    private Long gatewayId;

    /** 网关名称 */
    @TableField(value = "gateway_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String gatewayName;

    /** 网关描述 */
    @TableField("gateway_description")
    private String gatewayDescription;

    /** 分组名称 */
    @TableField(value = "group_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String groupName;

}

