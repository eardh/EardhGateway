package com.eardh.gateway.sdk.config.rest;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.eardh.gateway.sdk.annotation.Api;
import com.eardh.gateway.sdk.annotation.dubbo.ApiParam;
import com.eardh.gateway.sdk.annotation.rest.RestService;
import com.eardh.gateway.sdk.config.MicroserviceProperties;
import com.eardh.gateway.sdk.service.RestRpcRegisterService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static com.eardh.gateway.sdk.service.RpcRegisterService.MICROSERVICE_ID;

/**
 * @author eardh
 * @date 2023/4/13 14:56
 */
public class RestRegisterPostProcessor implements BeanPostProcessor {

    @Resource
    private MicroserviceProperties microserviceProperties;

    @Resource
    private RestRpcRegisterService registerService;

    @Resource
    private ConfigurableEnvironment environment;

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        String microserviceId = System.getProperty(MICROSERVICE_ID);
        if (StrUtil.isEmpty(microserviceId)) {
            return bean;
        }
        RestService restService = beanClass.getAnnotation(RestService.class);
        String restPath = environment.getProperty("server.servlet.context-path", "");
        if (ObjectUtil.isNotNull(restService)) {
            RequestMapping restMapping = beanClass.getAnnotation(RequestMapping.class);
            if (ObjectUtil.isNotNull(restMapping)) {
                restPath += mappingPath(restMapping.value());
            }

            String apiBasePath = microserviceProperties.getConfig().getMicroservicePath() + restService.api().path();
            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method : methods) {
                RestService methodRestService = method.getAnnotation(RestService.class);
                if (ObjectUtil.isNotNull(methodRestService)) {
                    String apiId = registerApi(methodRestService.api(), apiBasePath);
                    if (!methodRestService.api().mapping()) {
                        apiId = null;
                    }
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    String url = null;
                    String restMethod = null;
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof RequestMapping) {
                            url = restPath + mappingPath(((RequestMapping) annotation).value());
                            RequestMethod[] requestMethods = ((RequestMapping) annotation).method();
                            if (ArrayUtil.isNotEmpty(requestMethods)) {
                                restMethod = requestMethods[0].name().toLowerCase();
                            }
                        } else if (annotation instanceof GetMapping) {
                            url = restPath + mappingPath(((GetMapping) annotation).value());
                            restMethod = "get";
                        } else if (annotation instanceof PostMapping) {
                            url = restPath + mappingPath(((PostMapping) annotation).value());
                            restMethod = "post";
                        }
                        if (annotation instanceof PutMapping) {
                            url = restPath + mappingPath(((PutMapping) annotation).value());
                            restMethod = "put";
                        } else if (annotation instanceof DeleteMapping) {
                            url = restPath + mappingPath(((DeleteMapping) annotation).value());
                            restMethod = "delete";
                        }
                        if (annotation instanceof PatchMapping) {
                            url = restPath + mappingPath(((PatchMapping) annotation).value());
                            restMethod = "patch";
                        }
                    }
                    if (ObjectUtil.isNotNull(restMethod)) {
                        Map<String, Object> restInterfaceMap = new HashMap<>();
                        restInterfaceMap.put("apiId", apiId);
                        restInterfaceMap.put("microserviceId", Long.valueOf(microserviceId));
                        restInterfaceMap.put("restUrl", url);
                        restInterfaceMap.put("restMethod", restMethod);
                        restInterfaceMap.put("restDescription", methodRestService.description());

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
                        restInterfaceMap.put("restParametersType", String.join(",", parameterTypes));
                        restInterfaceMap.put("restParametersName", String.join(",", parameterNames));
                        restInterfaceMap.put("restResultType", method.getReturnType().getName());

                        registerService.registerRestRoute(restInterfaceMap);
                    }
                }
            }
        }
        return bean;
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

    private String mappingPath(String[] path) {
        if (ArrayUtil.isNotEmpty(path)) {
            if (path[0].startsWith("/")) {
                return path[0];
            } else {
                return "/" + path[0];
            }
        }
        return "";
    }
}
