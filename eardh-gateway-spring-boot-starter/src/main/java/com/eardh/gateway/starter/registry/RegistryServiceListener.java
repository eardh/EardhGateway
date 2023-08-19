package com.eardh.gateway.starter.registry;

import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author eardh
 * @date 2023/4/13 15:34
 */
@Slf4j
public class RegistryServiceListener implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    private RegistryService registryService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            registryService.initRegistry();
            registryService.registerInstance();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
