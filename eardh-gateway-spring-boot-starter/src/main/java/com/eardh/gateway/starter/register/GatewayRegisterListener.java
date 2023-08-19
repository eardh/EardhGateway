package com.eardh.gateway.starter.register;

import com.eardh.gateway.starter.register.service.GatewayRegisterService;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author eardh
 * @date 2023/4/1 16:04
 */
public class GatewayRegisterListener implements ApplicationListener<ApplicationStartedEvent> {

    private GatewayRegisterService gatewayRegisterService;

    public GatewayRegisterListener(GatewayRegisterService gatewayRegisterService) {
        this.gatewayRegisterService = gatewayRegisterService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        gatewayRegisterService.registerGatewayServer();
    }



}
