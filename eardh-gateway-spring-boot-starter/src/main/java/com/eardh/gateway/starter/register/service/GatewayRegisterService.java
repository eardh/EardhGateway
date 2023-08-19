package com.eardh.gateway.starter.register.service;

import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.starter.register.GatewayRegisterProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eardh
 * @date 2023/3/31 17:16
 */
@Slf4j
public class GatewayRegisterService {

    private GatewayRegisterProperties serviceProperties;

    /**
     * 网关注册uri
     */
    private String GATEWAY_REGISTER_URI = "/wg/admin/gateway/register-gateway";

    public GatewayRegisterService(GatewayRegisterProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public void registerGatewayServer() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayName", serviceProperties.getGatewayName());
        paramMap.put("gatewayDescription", serviceProperties.getGatewayDescription());
        paramMap.put("groupName", serviceProperties.getGroupName());
        String gatewayRegistry = serviceProperties.getGatewayRegistry();
        try {
            Result<Long> result = GateUtils.httpPost(gatewayRegistry,
                    GATEWAY_REGISTER_URI, paramMap,
                    new TypeReference<>() {
                    });
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("网关服务注册异常 {}", gatewayRegistry + GATEWAY_REGISTER_URI);
            throw new RuntimeException("网关服务注册异常", e);
        }
        log.info("网关服务注册成功 {}", serviceProperties.getGatewayName());
    }
}
