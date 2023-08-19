package com.eardh.gateway.sdk.config.rest;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;


public class RestBeanFactoryPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    private ConfigurableEnvironment environment;

    private final static String REST_PROPERTY_SOURCE_NAME = "rest_property_source_name";

    private final static String PORT_CONFIG = "eardh-gateway.microservice.config.port";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> map = new HashMap<>();
        String port = environment.getProperty(PORT_CONFIG);
        if (StrUtil.isBlank(port)) {
            port = String.valueOf(NetUtil.getUsableLocalPort(50000));
            map.put(PORT_CONFIG, port);
        }
        map.put("server.port", port);
        propertySources.addFirst(new MapPropertySource(REST_PROPERTY_SOURCE_NAME, map));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
