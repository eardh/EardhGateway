package com.eardh.gateway.center.model.rpc;

import com.baomidou.mybatisplus.annotation.*;
import com.eardh.gateway.center.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微服务
 * @author eardh
 * @date 2023/3/22 11:16
 */
@Data
@TableName("micro_service")
public class MicroService extends BaseModel {

    /** 微服务标识 */
    @TableId(value = "microservice_id", type = IdType.ASSIGN_ID)
    @EqualsAndHashCode.Exclude
    private Long microserviceId;

    /** 微服务名称 */
    @TableField(value = "microservice_name", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String microserviceName;

    /** 微服务描述 */
    @TableField("microservice_description")
    @EqualsAndHashCode.Exclude
    private String microserviceDescription;

    /** 微服务分组名称 */
    @TableField(value = "microservice_group", insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String microserviceGroup;

}
