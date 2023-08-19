package com.eardh.gateway.center.mapper.rpc.rest;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eardh.gateway.center.model.rpc.rest.RestInterface;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author eardh
 */
@Mapper
public interface RestInterfaceMapper extends BaseMapper<RestInterface> {

    Page<RestRoute> selectPage(Page<RestRoute> page, @Param("requireApiId") Boolean requireApiId);

    RestRoute selectByApiId(@Param("apiId") Long apiId);

    List<RestRoute> selectByMicroserviceId(@Param("microserviceId") Long microserviceId);

}




