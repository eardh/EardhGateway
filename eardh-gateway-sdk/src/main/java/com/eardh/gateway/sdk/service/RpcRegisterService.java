package com.eardh.gateway.sdk.service;

import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.sdk.config.MicroserviceProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;


/**
 * @author eardh
 * @date 2023/4/1 16:08
 */
@Slf4j
public abstract class RpcRegisterService implements InitializingBean {

    @Resource
    private MicroserviceProperties microserviceProperties;

    public final static String MICROSERVICE_ID = "microserviceId";

    protected String gatewayRegistry;

    private static final String MICROSERVICE_REGISTER_URI = "/wg/admin/rpc/register-microservice";

    private static final String API_REGISTER_URI = "/wg/admin/api/register-api";

    @Override
    public void afterPropertiesSet() {
        gatewayRegistry = microserviceProperties.getConfig().getGatewayRegistry();
        Map<String, Object> microserviceMap = new HashMap<>();
        microserviceMap.put("microserviceName", microserviceProperties.getName());
        microserviceMap.put("microserviceDescription", microserviceProperties.getDescription());
        microserviceMap.put("microserviceGroup", microserviceProperties.getGroup());
        String microserviceId = registerMicroservice(microserviceMap);
        System.setProperty(MICROSERVICE_ID, String.valueOf(microserviceId));
    }

    public String registerMicroservice(Map<String, Object> microserviceMap) {
        String id;
        try {
            Result<String> result = GateUtils.httpPost(gatewayRegistry, MICROSERVICE_REGISTER_URI, microserviceMap, new TypeReference<>() {
            });
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
            id = result.getData();
        } catch (Exception e) {
            log.error("应用服务注册异常 {}", gatewayRegistry + MICROSERVICE_REGISTER_URI);
            throw new RuntimeException("服务注册异常", e);
        }
        log.info("应用服务注册成功 {}", microserviceMap);
        return id;
    }

    public String registerGatewayApi(Map<String, Object> apiMap) {
        String apiId;
        try {
            Result<String> result = GateUtils.httpPost(gatewayRegistry, API_REGISTER_URI, apiMap, new TypeReference<>() {
            });
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
            apiId = result.getData();
        } catch (Exception e) {
            log.error("API注册异常 {}", gatewayRegistry + API_REGISTER_URI);
            throw new RuntimeException("API注册异常", e);
        }
        log.info("API注册成功 {}", apiMap);
        return apiId;
    }
}
