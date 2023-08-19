package com.eardh.gateway.sdk.config.dubbo;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.sdk.annotation.Api;
import com.eardh.gateway.sdk.annotation.dubbo.ApiParam;
import com.eardh.gateway.sdk.annotation.dubbo.DubboService;
import com.eardh.gateway.sdk.config.MicroserviceProperties;
import com.eardh.gateway.sdk.service.DubboRpcRegisterService;
import com.eardh.gateway.sdk.service.RpcRegisterService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解扫描，完成服务提供者注册
 *
 * @author eardh
 * @date 2023/4/1 15:43
 */
public class DubboRegisterPostProcessor implements BeanPostProcessor {

    @Resource
    private DubboRpcRegisterService registerService;

    @Resource
    private MicroserviceProperties microserviceProperties;

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public DubboRegisterPostProcessor(DubboRpcRegisterService registerService) {
        this.registerService = registerService;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        DubboService dubboService = beanClass.getAnnotation(DubboService.class);
        String interfaceId = null;
        if (ObjectUtil.isNotNull(dubboService)) {
            Map<String, Object> interfaceMap = new HashMap<>();
            interfaceMap.put("microserviceId", Long.valueOf(System.getProperty(RpcRegisterService.MICROSERVICE_ID)));
            interfaceMap.put("interfaceDescription", dubboService.description());
            interfaceMap.put("interfaceGroup", dubboService.group());
            Class<?> aClass = dubboService.$interface();
            if (aClass.getName().equals(DubboService.class.getName())) {
                interfaceMap.put("interfaceName", beanClass.getInterfaces()[0].getName());
                interfaceId = registerService.registerInterface(interfaceMap);
            } else {
                interfaceMap.put("interfaceName", dubboService.$interface().getName());
                interfaceId = registerService.registerInterface(interfaceMap);
            }
        }
        if (ObjectUtil.isNotNull(interfaceId)) {
            registerApiMethod(beanClass, interfaceId,
                    microserviceProperties
                            .getConfig()
                            .getMicroservicePath() + dubboService.api().path());
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void registerApiMethod(Class<?> beanClass, String interfaceId, String basePath) {
        Method[] methods = beanClass.getDeclaredMethods();
        for (Method method : methods) {
            DubboService dubboService = method.getAnnotation(DubboService.class);
            if (ObjectUtil.isNotNull(dubboService)) {
                String apiId = registerApi(dubboService.api(), basePath);
                if (!dubboService.api().mapping()) {
                    apiId = null;
                }
                Map<String, Object> methodMap = new HashMap<>();
                methodMap.put("apiId", apiId);
                methodMap.put("microserviceId", Long.valueOf(System.getProperty(RpcRegisterService.MICROSERVICE_ID)));
                methodMap.put("interfaceId", interfaceId);
                methodMap.put("methodName", method.getName());
                methodMap.put("methodDescription", dubboService.description());

                Parameter[] parameters = method.getParameters();
                String[] parameterNames = discoverer.getParameterNames(method);
                String[] parameterTypes = new String[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    ApiParam annotation = parameters[i].getAnnotation(ApiParam.class);
                    if (ObjectUtil.isNotNull(annotation)) {
                        parameterNames[i] = annotation.name();
                    }
                    parameterTypes[i] = parameters[i].getType().getName();
                }
                methodMap.put("methodParametersType", String.join(",", parameterTypes));
                methodMap.put("methodParametersName", String.join(",", parameterNames));
                methodMap.put("methodResultType", method.getReturnType().getName());

                registerService.registerMethod(methodMap);
            }
        }
    }

    private String registerApi(Api api, String basePath) {
        Map<String, Object> gatewayApiMap = new HashMap<>();
        gatewayApiMap.put("apiPath", basePath + api.path());
        gatewayApiMap.put("apiMethod", api.method());
        gatewayApiMap.put("apiProtocol", microserviceProperties.getConfig().getProtocol());
        gatewayApiMap.put("apiAuth", api.auth() ? "1" : "0");
        gatewayApiMap.put("apiDescription", api.description());
        return registerService.registerGatewayApi(gatewayApiMap);
    }
}
