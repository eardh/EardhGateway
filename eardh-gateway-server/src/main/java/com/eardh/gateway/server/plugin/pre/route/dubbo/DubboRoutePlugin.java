package com.eardh.gateway.server.plugin.pre.route.dubbo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.ErrorEnum;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.server.invocation.dubbo.DubboCallStatement;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.plugin.pre.route.extension.LoadBalance;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.route.dubbo.DubboRoute;
import com.eardh.gateway.server.storage.route.dubbo.DubboRouteStorage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * dubbo路由插件
 * @author eardh
 * @date 2023/3/26 20:28
 */
@Component
public class DubboRoutePlugin implements GatewayPlugin {

    public static final String Dubbo_CALL_STATEMENT = "dubboCallStatement";

    @Resource
    private DubboRouteStorage dubboRouteStorage;

    @Resource
    private LoadBalance loadBalance;

    @Override
    public boolean isSupport(HandlerContext handlerContext) {
        GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        return StrUtil.equalsAny(gatewayApi.getApiProtocol(), "dubbo", "tri");
    }

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        DubboRoute dubboRoute = dubboRouteStorage.getDubboRoute(gatewayApi.getApiId());
        if (ObjectUtil.isNull(dubboRoute)) {
            throw new GateWayException(ErrorEnum.API_NOT_PROVIDER);
        }

        // 根据负载均衡策略从nacos注册中心拉取主机
        Instance instance = loadBalance.select(handlerContext.getRequest(),
                dubboRoute.getMicroserviceName(), dubboRoute.getMicroserviceGroup());
        DubboCallStatement callStatement = new DubboCallStatement(instance);

        // 进行方法参数匹配解析
        String httpMethod = handlerContext.getRequest().getHttpMethod();
        if (StrUtil.equalsAny(httpMethod, "post", "patch", "put")) {
            callStatement.parseRequest(handlerContext.getRequest().getPostBody(), dubboRoute);
        } else {
            callStatement.parseRequest(handlerContext.getRequest().getParams(), dubboRoute);
        }

        // dubbo调用对象保存到处理上下文中
        handlerContext.setAttachment(Dubbo_CALL_STATEMENT, callStatement);
        chain.doExecute(handlerContext);
    }

    @Override
    public int order() {
        return 150;
    }

    @Override
    public String getName() {
        return "dubboRoutePlugin";
    }
}