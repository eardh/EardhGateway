package com.eardh.gateway.provider.test;

import com.eardh.gateway.provider.service.UserService;
import com.eardh.gateway.provider.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

import java.io.IOException;

/**
 * @author eardh
 * @date 2023/3/15 18:20
 */
@Slf4j
public class Provider {
    public static void main(String[] args) throws IOException {

        ServiceConfig<UserService> service1 = new ServiceConfig<>();
        service1.setInterface(UserService.class);
        service1.setRef(new UserServiceImpl());
        service1.setGroup("test-user");

        ApplicationConfig applicationConfig = new ApplicationConfig("eardh-gateway-test-provider2");
        applicationConfig.setRegisterMode("instance");

        ProtocolConfig protocolConfig = new ProtocolConfig(CommonConstants.TRIPLE, 50051);
        protocolConfig.setSerialization("fastjson2");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig)
                .protocol(protocolConfig)
                .service(service1)
                .start();

        System.in.read();
    }
}
