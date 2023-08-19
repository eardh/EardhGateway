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
public class DubboRpcRegisterService extends RpcRegisterService {

    private static final String INTERFACE_REGISTER_URI = "/wg/admin/rpc/register-dubbo_interface";

    private static final String METHOD_REGISTER_URI = "/wg/admin/rpc/register-dubbo_method";

    public String registerInterface(Map<String, Object> interfaceMap) {
        String id;
        try {
            Result<String> result = GateUtils.httpPost(gatewayRegistry, INTERFACE_REGISTER_URI, interfaceMap, new TypeReference<>() {});
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
            id = result.getData();
        } catch (Exception e) {
            log.error("接口服务注册异常 {}",  gatewayRegistry + INTERFACE_REGISTER_URI);
            throw new RuntimeException("服务注册异常", e);
        }
        log.info("接口服务注册成功 {}", interfaceMap);
        return id;
    }

    public String registerMethod(Map<String, Object> methodMap) {
        String id;
        try {
            Result<String> result = GateUtils.httpPost(gatewayRegistry, METHOD_REGISTER_URI, methodMap, new TypeReference<>() {});
            if (!result.getCode().equals("0000")) {
                throw new Exception();
            }
            id = result.getData();
        } catch (Exception e) {
            log.error("接口方法注册异常 {}",  gatewayRegistry + METHOD_REGISTER_URI);
            throw new RuntimeException("服务注册异常", e);
        }
        log.info("接口方法注册成功 {}", methodMap);
        return id;
    }

}
