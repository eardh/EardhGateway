package com.eardh.gateway.core.plugin;

import com.eardh.gateway.core.HandlerContext;

/**
 * @author eardh
 * @date 2023/3/25 16:04
 */
public interface GatewayPlugin {

    default void handle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        if (!isSupport(handlerContext)) {
            chain.doExecute(handlerContext);
        } else {
            doHandle(handlerContext, chain);
        }
    }

    /**
     * 是否支持该插件
     * @param handlerContext 网关交换类
     * @return 是否支持
     */
    default boolean isSupport(HandlerContext handlerContext) {
        return true;
    }

    /**
     * 插件的业务处理逻辑
     * @param handlerContext 网关交换类
     * @param chain 插件链
     * @throws Exception 异常
     */
    void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception;

    /**
     * order 决定了插件执行顺序<br>
     * 规定：<br>
     *   - 路由前逻辑插件 0 - 99<br>
     *   - 路由插件 100 - 199<br>
     *   - 路由后插件 200+
     * @return 获取排序
     */
    int order();

    /**
     * 返回组件名称
     *
     * @return 返回结果
     */
    String getName();

}
