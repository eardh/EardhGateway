package com.eardh.gateway.center.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.eardh.gateway.center.config.GlobalConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author eardh
 * @date 2023/4/17 15:17
 */
@Service
public class GlobalConfigService implements InitializingBean {

    @Value("${nacos.address}")
    private String nacosAddress;

    @Value("${nacos.data-id}")
    private String dataId;

    @Value("${nacos.group}")
    private String group;

    private ConfigService configService;

    private GlobalConfig globalConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigService configService = NacosFactory.createConfigService(nacosAddress);
        String config = configService.getConfig(dataId, group, 10000);
        this.configService = configService;
        this.globalConfig = JSON.parseObject(config, GlobalConfig.class);
    }

    public void addListener(Listener listener) throws NacosException {
        configService.addListener(dataId, group, listener);
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }
}


