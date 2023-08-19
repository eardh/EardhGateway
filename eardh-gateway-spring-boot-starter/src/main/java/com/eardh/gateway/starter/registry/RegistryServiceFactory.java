package com.eardh.gateway.starter.registry;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.starter.configuration.GatewayServerProperties;
import com.eardh.gateway.starter.register.GatewayRegisterProperties;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/5 13:00
 */
public class RegistryServiceFactory {

    private static final String GLOBAL_CONFIG_API = "/wg/admin/gateway/global-config";

    public static RegistryService getRegistryService(GatewayRegisterProperties registerProperties, GatewayServerProperties serverProperties) throws Exception {
        String rpcRegistry = getRpcRegistry(registerProperties);
        String protocol = GateUtils.getProtocol(rpcRegistry);
        switch (RegistryType.valueOf(protocol)) {
            case nacos -> {
                return new NacosRegistryService(registerProperties, serverProperties, GateUtils.getAddress(rpcRegistry));
            }
            default -> {
                return null;
            }
        }
    }

    private static String getRpcRegistry(GatewayRegisterProperties registerProperties) throws Exception {
        Result<Map<String, Object>> result = GateUtils.httpGet(registerProperties.getGatewayRegistry(), GLOBAL_CONFIG_API, null, new TypeReference<>() {});
        if (ObjectUtil.isNotNull(result.getData())) {
            return String.valueOf(result.getData().get("registry"));
        }
        return null;
    }

    public enum RegistryType {

        nacos("nacos"),zk("zookeeper");

        private final String name;

        RegistryType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
