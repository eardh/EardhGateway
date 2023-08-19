package com.eardh.gateway.center.repository;

import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import com.eardh.gateway.center.model.rpc.vo.MicroServiceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboInterfaceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboMethodVO;
import com.eardh.gateway.center.model.rpc.vo.RestInterfaceVO;

/**
 * @author eardh
 * @date 2023/3/23 15:20
 */
public interface IRpcManageRepository {

    MicroServiceVO registerMicroservice(MicroServiceVO microServiceVO);

    PageVO<MicroServiceVO> getMicroservices(Integer currentPage, Integer pageSize);

    DubboInterfaceVO registerDubboInterface(DubboInterfaceVO dubboInterfaceVO);

    DubboMethodVO registerDubboMethod(DubboMethodVO dubboMethodVO);

    RestInterfaceVO registerRestInterface(RestInterfaceVO restInterfaceVO);

    PageVO<RestRoute> getRestRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId);

    RestRoute getRestRoute(Long apiId);

    PageVO<DubboRoute> getDubboRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId);

    DubboRoute getDubboRoute(Long apiId);

    MicroServiceVO getApplication(String microserviceId);
}