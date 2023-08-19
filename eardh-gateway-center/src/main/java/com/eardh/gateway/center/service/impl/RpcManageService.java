package com.eardh.gateway.center.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.center.config.GlobalConfig;
import com.eardh.gateway.center.model.PageVO;
import com.eardh.gateway.center.model.aggregator.DubboRoute;
import com.eardh.gateway.center.model.aggregator.RestRoute;
import com.eardh.gateway.center.model.gateway.vo.GatewayServerVO;
import com.eardh.gateway.center.model.rpc.ApplicationInstance;
import com.eardh.gateway.center.model.rpc.vo.MicroServiceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboInterfaceVO;
import com.eardh.gateway.center.model.rpc.vo.DubboMethodVO;
import com.eardh.gateway.center.model.rpc.vo.RestInterfaceVO;
import com.eardh.gateway.center.repository.IRpcManageRepository;
import com.eardh.gateway.center.service.GlobalConfigService;
import com.eardh.gateway.center.service.IRpcManageService;
import com.eardh.gateway.common.util.GateUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author eardh
 * @date 2023/3/23 15:26
 */
@Service
public class RpcManageService implements IRpcManageService, InitializingBean {

    @Resource
    private IRpcManageRepository rpcManageRepository;

    @Resource
    private GlobalConfigService globalConfigService;

    @Resource
    private GatewayManageService gatewayManageService;

    private NamingService namingService;

    private String registry;

    @Override
    public MicroServiceVO registerMicroservice(MicroServiceVO microServiceVO) {
        return rpcManageRepository.registerMicroservice(microServiceVO);
    }

    @Override
    public PageVO<MicroServiceVO> getMicroservices(Integer currentPage, Integer pageSize) {
        PageVO<MicroServiceVO> microservices = rpcManageRepository.getMicroservices(currentPage, pageSize);
        return microservices;
    }

    @Override
    public DubboInterfaceVO registerDubboInterface(DubboInterfaceVO dubboInterfaceVO) {
        return rpcManageRepository.registerDubboInterface(dubboInterfaceVO);
    }

    @Override
    public DubboMethodVO registerDubboMethod(DubboMethodVO dubboMethodVO) {
        return rpcManageRepository.registerDubboMethod(dubboMethodVO);
    }

    @Override
    public PageVO<DubboRoute> getDubboRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId) {
        return rpcManageRepository.getDubboRoutes(currentPage, pageSize, requireApiId);
    }

    @Override
    public RestInterfaceVO registerRestRoute(RestInterfaceVO restInterfaceVO) {
        return rpcManageRepository.registerRestInterface(restInterfaceVO);
    }

    @Override
    public PageVO<RestRoute> getRestRoutes(Integer currentPage, Integer pageSize, Boolean requireApiId) {
        return rpcManageRepository.getRestRoutes(currentPage, pageSize, requireApiId);
    }

    @Override
    public void updateRestRoute(RestRoute restRoute) {
        MicroServiceVO microServiceVO = BeanUtil.toBean(restRoute, MicroServiceVO.class);
        MicroServiceVO microServiceVO1 = rpcManageRepository.registerMicroservice(microServiceVO);
        if (ObjUtil.isNotNull(microServiceVO1)
                && StrUtil.isNotBlank(microServiceVO1.getMicroserviceId())) {
            restRoute.setMicroserviceId(microServiceVO1.getMicroserviceId());
            RestInterfaceVO restInterfaceVO = BeanUtil.toBean(restRoute, RestInterfaceVO.class);
            rpcManageRepository.registerRestInterface(restInterfaceVO);
        }
    }

    @Override
    public void updateDubboRoute(DubboRoute dubboRoute) {
        MicroServiceVO microServiceVO = BeanUtil.toBean(dubboRoute, MicroServiceVO.class);
        MicroServiceVO microServiceVO1 = rpcManageRepository.registerMicroservice(microServiceVO);
        if (ObjUtil.isNotNull(microServiceVO1)
                && StrUtil.isNotBlank(microServiceVO1.getMicroserviceId())) {
            dubboRoute.setMicroserviceId(microServiceVO1.getMicroserviceId());
            DubboInterfaceVO dubboInterfaceVO = BeanUtil.toBean(dubboRoute, DubboInterfaceVO.class);
            DubboInterfaceVO dubboInterfaceVO1 = rpcManageRepository.registerDubboInterface(dubboInterfaceVO);
            if (ObjUtil.isNotNull(dubboInterfaceVO1)
                    && StrUtil.isNotBlank(dubboInterfaceVO1.getInterfaceId())) {
                dubboRoute.setInterfaceId(dubboInterfaceVO1.getInterfaceId());
                DubboMethodVO dubboMethodVO = BeanUtil.toBean(dubboRoute, DubboMethodVO.class);
                rpcManageRepository.registerDubboMethod(dubboMethodVO);
            }
        }
    }

    @Override
    public List<ApplicationInstance> getApplicationInstances(String microserviceId) throws NacosException {
        MicroServiceVO microServiceVO = rpcManageRepository.getApplication(microserviceId);
        List<ApplicationInstance> list = new ArrayList<>();
        List<Instance> instances = namingService.getAllInstances(microServiceVO.getMicroserviceName(), microServiceVO.getMicroserviceGroup());
        for (Instance instance : instances) {
            ApplicationInstance applicationInstance = new ApplicationInstance();
            applicationInstance.setIp(instance.getIp());
            applicationInstance.setPort(instance.getPort());
            applicationInstance.setHealthy(instance.isHealthy());
            applicationInstance.setVersion(instance.getMetadata().get("version"));
            applicationInstance.setWeight(instance.getWeight());
            applicationInstance.setEnable(instance.isEnabled());
            list.add(applicationInstance);
        }
        return list;
    }

    @Override
    public List<ApplicationInstance> getGatewayInstances(String gatewayId) throws NacosException {
        GatewayServerVO gatewayServer = gatewayManageService.getGatewayServer(gatewayId);
        List<ApplicationInstance> list = new ArrayList<>();
        List<Instance> instances = namingService.getAllInstances(gatewayServer.getGatewayName(), gatewayServer.getGroupName());
        for (Instance instance : instances) {
            ApplicationInstance applicationInstance = new ApplicationInstance();
            applicationInstance.setIp(instance.getIp());
            applicationInstance.setPort(instance.getPort());
            applicationInstance.setHealthy(instance.isHealthy());
            applicationInstance.setEnable(instance.isEnabled());
            list.add(applicationInstance);
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registry = globalConfigService.getGlobalConfig().getRegistry();
        this.namingService = NacosFactory.createNamingService(GateUtils.getAddress(registry));
        globalConfigService.addListener(new AbstractListener() {

            @Override
            public void receiveConfigInfo(String configInfo) {
                GlobalConfig globalConfig = JSONObject.parseObject(configInfo, GlobalConfig.class);
                if (!StrUtil.equals(globalConfig.getRegistry(), registry)) {
                    registry = globalConfig.getRegistry();
                    try {
                        namingService.shutDown();
                    } catch (NacosException e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            namingService = NacosFactory.createNamingService(GateUtils.getAddress(registry));
                        } catch (NacosException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }
}
