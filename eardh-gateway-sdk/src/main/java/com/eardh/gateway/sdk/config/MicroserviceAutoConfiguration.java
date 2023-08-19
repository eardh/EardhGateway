package com.eardh.gateway.sdk.config;

import com.eardh.gateway.sdk.config.dubbo.DubboRegisterPostProcessor;
import com.eardh.gateway.sdk.config.dubbo.DubboRpcServiceListener;
import com.eardh.gateway.sdk.config.rest.RestBeanFactoryPostProcessor;
import com.eardh.gateway.sdk.config.rest.RestRegisterPostProcessor;
import com.eardh.gateway.sdk.registry.RegistryService;
import com.eardh.gateway.sdk.registry.RegistryServiceFactory;
import com.eardh.gateway.sdk.registry.RegistryServiceListener;
import com.eardh.gateway.sdk.rpc.DubboRpcExposeService;
import com.eardh.gateway.sdk.service.DubboRpcRegisterService;
import com.eardh.gateway.sdk.service.RestRpcRegisterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author eardh
 * @date 2023/3/31 16:16
 */
@Configuration
@EnableConfigurationProperties(value = {MicroserviceProperties.class})
public class MicroserviceAutoConfiguration {

    /**
     * dubbo环境下的rpc服务提供配置
     */
    @Configuration
    @ConditionalOnExpression("#{'${eardh-gateway.microservice.config.protocol}'.equalsIgnoreCase('dubbo') or '${eardh-gateway.microservice.config.protocol}'.equals('tri')}")
    static class DubboCondition {

        @Configuration
        @ConditionalOnProperty(name = "eardh-gateway.microservice.report-enable", havingValue = "true")
        @Import(value = {DubboRegisterPostProcessor.class, DubboRpcRegisterService.class})
        static class ApiRegisterCondition {
        }

        @Configuration
        @ConditionalOnProperty(name = "eardh-gateway.microservice.rpc-service-enable", havingValue = "true")
        @Import(value = {DubboRpcServiceListener.class, DubboRpcExposeService.class})
        static class RpcRegisterCondition {
        }
    }

    /**
     * springweb环境下rpc服务提供配置
     */
    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnExpression("#{'${eardh-gateway.microservice.config.protocol}'.equalsIgnoreCase('http')}")
    static class RestCondition {

        @Configuration
        @ConditionalOnProperty(name = "eardh-gateway.microservice.report-enable", havingValue = "true")
        @Import(value = {RestRegisterPostProcessor.class, RestRpcRegisterService.class})
        static class ApiRegisterCondition {
        }

        @Configuration
        @ConditionalOnProperty(name = "eardh-gateway.microservice.rpc-service-enable", havingValue = "true")
        @Import(value = {RestBeanFactoryPostProcessor.class})
        static class RpcRegisterCondition {
        }
    }

    /**
     * 应用注册中心服务配置
     */
    @Configuration
    @ConditionalOnProperty(name = "eardh-gateway.microservice.registry-center-enable", havingValue = "true")
    @Import(value = {RegistryServiceListener.class})
    static class RegistryCondition {

        @Bean
        @ConditionalOnMissingBean
        public RegistryService registryService(MicroserviceProperties microserviceProperties) throws Exception {
            return RegistryServiceFactory.getRegistryService(microserviceProperties);
        }
    }
}
