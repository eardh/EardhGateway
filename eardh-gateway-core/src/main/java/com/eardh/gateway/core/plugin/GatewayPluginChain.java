package com.eardh.gateway.core.plugin;

import com.eardh.gateway.core.HandlerContext;

/**
 * 插件链
 * @author eardh
 * @date 2023/3/25 16:31
 */
public interface GatewayPluginChain {

    /**
     * 执行插件链
     * @param handlerExchange
     * @throws Exception 插件执行异常
     */
    void doExecute(HandlerContext handlerExchange) throws Exception;

}
