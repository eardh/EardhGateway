package com.eardh.gateway.server.plugin.pre.api;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.api.GatewayApiStorage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import static com.eardh.gateway.core.exception.ErrorEnum.WRONG_API_EXCEPTION;

/**
 * 获取网关API资源
 * @author eardh
 * @date 2023/4/3 16:10
 */
@Component
public class GatewayApiPlugin implements GatewayPlugin {

    public static final String GATEWAY_API_KEY = "gateway_api";

    @Resource
    private GatewayApiStorage gatewayApiStorage;

    @Override
public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
    Request request = handlerContext.getRequest();
    String uri = request.getUri();
    String httpMethod = request.getHttpMethod();
    GatewayApi gatewayApi = gatewayApiStorage.getGatewayApi(uri, httpMethod);
    if (ObjectUtil.isNull(gatewayApi)) {
        throw new GateWayException(WRONG_API_EXCEPTION);
    }
    handlerContext.setAttachment(GATEWAY_API_KEY, gatewayApi);
    chain.doExecute(handlerContext);
}

    @Override
    public int order() {
        return 0;
    }

    @Override
    public String getName() {
        return "apiPlugin";
    }
}
