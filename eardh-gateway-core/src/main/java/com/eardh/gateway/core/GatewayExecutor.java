package com.eardh.gateway.core;

import com.eardh.gateway.core.exception.handler.ExceptionHandler;
import com.eardh.gateway.core.plugin.DefaultGatewayPluginChain;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Response;
import com.eardh.gateway.core.wapper.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author eardh
 * @date 2023/3/30 15:44
 */
@Slf4j
public class GatewayExecutor {

    private List<GatewayPlugin> gatewayPlugins;

    private ExceptionHandler exceptionHandler;

    public GatewayExecutor(List<GatewayPlugin> gatewayPlugins, ExceptionHandler exceptionHandler) {
        this.gatewayPlugins = gatewayPlugins;
        this.exceptionHandler = exceptionHandler;
    }

    public Response execute(Request request) {
        HandlerContext handlerContext = new DefaultHandlerExchange(request, new Response());
        GatewayPluginChain pluginChain = new DefaultGatewayPluginChain(gatewayPlugins);
        try {
            pluginChain.doExecute(handlerContext);
        } catch (Exception e) {
            log.error(e.getMessage());
            exceptionHandler.handle(e, handlerContext);
        }
        return handlerContext.getResponse();
    }
}
