package com.eardh.gateway.center.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * PO基类
 * @author eardh
 * @date 2023/3/28 14:10
 */
@Data
public class BaseModel {

    /** 创建时间 */
    @EqualsAndHashCode.Exclude
    @TableField(value = "create_time", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private Date createTime;

    /** 更新时间 */
    @EqualsAndHashCode.Exclude
    @TableField(value = "update_time", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private Date updateTime;

}
