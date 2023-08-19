package com.eardh.gateway.sdk.config.dubbo;

import com.eardh.gateway.sdk.annotation.dubbo.DubboService;
import com.eardh.gateway.sdk.registry.RegistryService;
import com.eardh.gateway.sdk.rpc.RpcExposeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eardh
 * @date 2023/4/4 21:13
 */
@Slf4j
public class DubboRpcServiceListener implements ApplicationListener<ApplicationStartedEvent> {

    private RpcExposeService rpcExposeService;

    public DubboRpcServiceListener(RpcExposeService rpcExposeService, RegistryService registryService) {
        this.rpcExposeService = rpcExposeService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        String[] names = applicationContext.getBeanNamesForAnnotation(DubboService.class);
        List<Object> beans = new ArrayList<>();
        for (String name : names) {
            beans.add(applicationContext.getBean(name));
        }
        rpcExposeService.exposeServices(beans);
        log.info("dubbo服务启动成功");
    }
}
