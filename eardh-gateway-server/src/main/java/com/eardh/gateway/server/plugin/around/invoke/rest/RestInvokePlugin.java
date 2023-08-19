package com.eardh.gateway.server.plugin.around.invoke.rest;

import cn.hutool.core.util.StrUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.invocation.RpcInvocation;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.storage.api.GatewayApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author eardh
 * @date 2023/4/13 22:24
 */
@Component
public class RestInvokePlugin implements GatewayPlugin {

    @Resource(name = "restRpcInvocation")
    private RpcInvocation rpcInvocation;

    @Override
    public boolean isSupport(HandlerContext handlerContext) {
        GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        return StrUtil.equalsAny(gatewayApi.getApiProtocol(), "http", "https");
    }

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        rpcInvocation.doInvoke(handlerContext);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE - 10;
    }

    @Override
    public String getName() {
        return "restInvokePlugin";
    }

}
