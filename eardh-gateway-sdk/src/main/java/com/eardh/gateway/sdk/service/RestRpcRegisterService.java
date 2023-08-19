package com.eardh.gateway.sdk.service;

import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/13 16:23
 */
@Slf4j
public class RestRpcRegisterService extends RpcRegisterService {

    private final static String REST_ROUTE_REGISTER_URI = "/wg/admin/rpc/register-rest-route";

    public String registerRestRoute(Map<String, Object> restInterfaceMap) {
        String id;
        try {
            Result<String> result = GateUtils.httpPost(gatewayRegistry, REST_ROUTE_REGISTER_URI, restInterfaceMap, new TypeReference<>() {});
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
            id = result.getData();
        } catch (Exception e) {
            log.error("REST服务注册异常 {}",  gatewayRegistry + REST_ROUTE_REGISTER_URI);
            throw new RuntimeException("服务注册异常", e);
        }
        log.info("REST服务注册成功 {}", restInterfaceMap);
        return id;
    }
}
