package com.eardh.gateway.center.service;


import com.alibaba.nacos.api.exception.NacosException;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import com.eardh.gateway.center.model.rpc.ApplicationInstance;
import com.eardh.gateway.center.model.rpc.vo.MicroServiceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboInterfaceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboMethodVO;
import com.eardh.gateway.center.model.rpc.vo.RestInterfaceVO;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/23 15:23
 */
public interface IRpcManageService {

    MicroServiceVO registerMicroservice(MicroServiceVO microServiceVO);

    PageVO<MicroServiceVO> getMicroservices(Integer currentPage, Integer pageSize);

    DubboInterfaceVO registerDubboInterface(DubboInterfaceVO dubboInterfaceVO);

    DubboMethodVO registerDubboMethod(DubboMethodVO dubboMethodVO);

    PageVO<DubboRoute> getDubboRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId);

    RestInterfaceVO registerRestRoute(RestInterfaceVO restRoutePO);

    PageVO<RestRoute> getRestRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId);

    void updateRestRoute(RestRoute restRoute);

    void updateDubboRoute(DubboRoute dubboRoute);

    List<ApplicationInstance> getApplicationInstances(String microserviceId) throws NacosException;

    List<ApplicationInstance> getGatewayInstances(String gatewayId) throws NacosException;
}
