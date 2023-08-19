package com.eardh.gateway.starter.configuration;

import com.eardh.gateway.core.GatewayExecutor;
import com.eardh.gateway.core.GatewayServer;
import com.eardh.gateway.core.exception.handler.DefaultExceptionHandler;
import com.eardh.gateway.core.exception.handler.ExceptionHandler;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.server.HttpServer;
import com.eardh.gateway.core.server.netty.NettyHttpServer;
import com.eardh.gateway.core.server.netty.config.NettyConfig;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/30 15:15
 */
@Configuration
@Import(value = {GatewayServerStartStopLifecycle.class})
@EnableConfigurationProperties(GatewayServerProperties.class)
public class GatewayServerAutoConfiguration {

    @Resource
    private GatewayServerProperties gatewayServerProperties;

    @Bean
    @ConditionalOnMissingBean(value = {ExceptionHandler.class})
    public ExceptionHandler exceptionHandler() {
        return new DefaultExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(value = {HttpServer.class})
    public HttpServer nettyHttpServer(List<GatewayPlugin> gatewayPlugins, ExceptionHandler exceptionHandler) {
        gatewayPlugins.sort((o1, o2) -> {
            if (o1.order() > o2.order()) {
                return 1;
            } else if (o1.order() < o2.order()) {
                return -1;
            }
            return 0;
        });
        NettyConfig nettyConfig = new NettyConfig();
        nettyConfig.setPort(gatewayServerProperties.getPort());
        nettyConfig.setAggregator(gatewayServerProperties.getNetty().getAggregator());
        nettyConfig.setSo_backLog(gatewayServerProperties.getNetty().getSo_backLog());
        nettyConfig.setBossGroupNThread(gatewayServerProperties.getNetty().getBossGroupNThread());
        nettyConfig.setWorkerGroupNThread(gatewayServerProperties.getNetty().getWorkerGroupNThread());
        GatewayExecutor gatewayExecutor = new GatewayExecutor(gatewayPlugins, exceptionHandler);
        NettyHttpServer httpServer = new NettyHttpServer(nettyConfig, gatewayExecutor);
        return httpServer;
    }

    @Bean
    @ConditionalOnMissingBean(value = {GatewayServer.class})
    public GatewayServer gatewayServer(HttpServer httpServer) {
        GatewayServer gatewayServer = new GatewayServer(httpServer);
        return gatewayServer;
    }
}
