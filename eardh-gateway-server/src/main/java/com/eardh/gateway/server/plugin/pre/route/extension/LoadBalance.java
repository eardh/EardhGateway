package com.eardh.gateway.server.plugin.pre.route.extension;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.core.exception.ErrorEnum;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.storage.config.GlobalConfigStorage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author eardh
 * @date 2023/4/8 21:05
 */
@Slf4j
public abstract class LoadBalance implements InitializingBean {

    @Resource
    private GlobalConfigStorage globalConfigStorage;

    protected NamingService namingService;

    @Override
    public void afterPropertiesSet() {
        String registry = String.valueOf(globalConfigStorage.getGlobalConfig().getRegistry());
        // 默认网关注册中心
        NamingService namingService;
        try {
            namingService = NacosFactory.createNamingService(GateUtils.getAddress(registry));
        } catch (NacosException e) {
            log.error("注册中心服务创建错误");
            throw new RuntimeException(e);
        }
        this.namingService = namingService;
    }

    /**
     * 负载均衡选择路由地址
     * @param request 请求对象
     * @param serviceName 服务名
     * @param serviceGroup 服务组
     * @return 选择的实例
     */
    public Instance select(Request request, String serviceName, String serviceGroup) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName, serviceGroup);
            if (CollUtil.isEmpty(instances)) {
                throw new GateWayException(ErrorEnum.API_NOT_PROVIDER);
            }
            Instance node = doSelect(request, instances);
            assert node != null;
            return node;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 负载均衡选择
     * @param requestWrapper 请求
     * @param instances 节点
     * @return 负载得到的实例
     */
    protected abstract Instance doSelect(Request requestWrapper, List<Instance> instances);
}
