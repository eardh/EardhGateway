package com.eardh.gateway.center.model.rpc.dubbo;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;

/**
 * Dubbo协议接口类信息
 * @author eardh
 * @date 2023/3/22 13:29
 */
@Data
@TableName("dubbo_interface")
public class DubboInterface extends BaseModel {

    /** 接口标识 */
    @TableId(value = "interface_id", type = IdType.ASSIGN_ID)
    private Long interfaceId;

    /** 微服务标识 */
    @TableField(value = "microservice_id", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long microserviceId;

    /** 接口名称 */
    @TableField(value = "interface_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String interfaceName;

    /** 接口描述 */
    @TableField("interface_description")
    private String interfaceDescription;

    /** 接口分组名称 */
    @TableField(value = "interface_group", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String interfaceGroup;

}

