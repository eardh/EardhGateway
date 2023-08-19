package com.eardh.gateway.core.plugin;

import com.eardh.gateway.core.HandlerContext;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/25 16:42
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain {

    private final List<GatewayPlugin> gatewayPlugins;
    private int index = 0;

    public DefaultGatewayPluginChain(List<GatewayPlugin> gatewayPlugins) {
        this.gatewayPlugins = gatewayPlugins;
    }

    @Override
    public void doExecute(HandlerContext handlerExchange) throws Exception {
        if (gatewayPlugins.size() > index) {
            gatewayPlugins.get(index++).handle(handlerExchange, this);
        }
    }
}
