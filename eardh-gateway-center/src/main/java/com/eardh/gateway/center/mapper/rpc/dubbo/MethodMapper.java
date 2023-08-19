package com.eardh.gateway.center.mapper.rpc.dubbo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.rpc.dubbo.DubboMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/22 13:56
 */
@Mapper
public interface MethodMapper extends BaseMapper<DubboMethod> {

    Page<DubboRoute> selectPage(Page<DubboRoute> page, @Param("requireApiId") Boolean requireApiId);

    DubboRoute selectByApiId(@Param("apiId") Long apiId);

    List<DubboRoute> selectByMicroserviceIdIfInterfaceId(@Param("microserviceId") Long microserviceId,
                                                         @Param("interfaceId") Long interfaceId);

}
