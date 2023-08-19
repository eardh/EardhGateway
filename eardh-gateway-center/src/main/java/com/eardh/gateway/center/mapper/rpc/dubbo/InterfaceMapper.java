package com.eardh.gateway.center.mapper.rpc.dubbo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eardh.gateway.center.model.rpc.dubbo.DubboInterface;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author eardh
 * @date 2023/3/22 13:54
 */
@Mapper
public interface InterfaceMapper extends BaseMapper<DubboInterface> {

}