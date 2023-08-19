package com.eardh.gateway.starter.register;

import com.eardh.gateway.starter.configuration.GatewayServerProperties;
import com.eardh.gateway.starter.register.service.GatewayRegisterService;
import com.eardh.gateway.starter.registry.RegistryService;
import com.eardh.gateway.starter.registry.RegistryServiceFactory;
import com.eardh.gateway.starter.registry.RegistryServiceListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author eardh
 * @date 2023/4/1 15:51
 */
@Configuration
@EnableConfigurationProperties(value = {GatewayRegisterProperties.class})
public class GatewayRegisterAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "eardh-gateway.gateway.register.enable", havingValue = "true")
    static class GatewayRegister {
        @Bean
        public GatewayRegisterService gatewayRegisterService(GatewayRegisterProperties serviceProperties) {
            return new GatewayRegisterService(serviceProperties);
        }

        @Bean
        public GatewayRegisterListener gatewayServiceListener(GatewayRegisterProperties serviceProperties) {
            return new GatewayRegisterListener(gatewayRegisterService(serviceProperties));
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "eardh-gateway.gateway.register.enable-registry", havingValue = "true")
    @Import(value = {RegistryServiceListener.class})
    static class GatewayRegistry {

        @Bean
        public RegistryService registryService(GatewayRegisterProperties serviceProperties,
                                               GatewayServerProperties serverProperties) throws Exception {
            return RegistryServiceFactory.getRegistryService(serviceProperties, serverProperties);
        }
    }
}
