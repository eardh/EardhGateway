package com.eardh.gateway.server.plugin.pre.route.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.ErrorEnum;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.server.invocation.rest.RestCallStatement;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.plugin.pre.route.extension.LoadBalance;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.route.rest.RestRoute;
import com.eardh.gateway.server.storage.route.rest.RestRouteStorage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author eardh
 * @date 2023/4/14 14:13
 */
@Component
public class RestRoutePlugin implements GatewayPlugin {

    public static final String REST_CALL_STATEMENT = "restCallStatement";

    @Resource
    private RestRouteStorage restRouteStorage;

    @Resource
    private LoadBalance loadBalance;

    @Override
    public boolean isSupport(HandlerContext handlerContext) {
        GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        return StrUtil.equalsAny(gatewayApi.getApiProtocol(), "http", "https");
    }

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        // 从sql获取 api - 服务信息
        GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        RestRoute restRoute = restRouteStorage.getRestRoute(gatewayApi.getApiId());
        if (ObjectUtil.isNull(restRoute)) {
            throw new GateWayException(ErrorEnum.API_NOT_PROVIDER);
        }

        // 根据服务从注册中心拉取服务并用负载均衡算法拉取一台主机
        Instance instance = loadBalance.select(handlerContext.getRequest(), restRoute.getMicroserviceName(), restRoute.getMicroserviceGroup());
        RestCallStatement restCallStatement = new RestCallStatement(instance);
        restCallStatement.setUrl(restRoute.getRestUrl());
        restCallStatement.setMethod(restRoute.getRestMethod());

        handlerContext.setAttachment(REST_CALL_STATEMENT, restCallStatement);
        chain.doExecute(handlerContext);
    }

    @Override
    public int order() {
        return 150;
    }

    @Override
    public String getName() {
        return "restRoutePlugin";
    }
}
