package com.eardh.gateway.server.invocation.dubbo;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.invocation.RpcInvocation;
import com.eardh.gateway.core.util.Result;
import com.eardh.gateway.server.plugin.pre.route.dubbo.DubboRoutePlugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.FrameworkModel;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.eardh.gateway.common.util.GateUtils.buildUri;
import static org.apache.dubbo.common.constants.CommonConstants.GENERIC_SERIALIZATION_DEFAULT;

/**
 * @author eardh
 * @date 2023/3/27 11:13
 */
@Slf4j
@Component("dubboRpcInvocation")
public class DubboInvocation implements RpcInvocation, InitializingBean {

    @Value("${eardh-gateway.gateway.register.gateway-name}")
    private String gatewayName;

    private ApplicationConfig applicationConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationModel applicationModel = FrameworkModel.defaultModel().newApplication();
        this.applicationConfig = new ApplicationConfig(gatewayName);
        applicationConfig.setQosEnable(false);
        applicationModel.getApplicationConfigManager().setApplication(applicationConfig);
        applicationModel.getDeployer().start();
    }

    @Override
public void doInvoke(HandlerContext handlerContext) {
    DubboCallStatement callStatement = handlerContext.getAttachment(DubboRoutePlugin.Dubbo_CALL_STATEMENT);
    String uri = buildUri(callStatement.getProtocol(), callStatement.getIp(), callStatement.getPort());
    GenericService genericService = getGenericService(callStatement, uri);
    Object result = genericService.$invoke(callStatement.getMethodName(),
            callStatement.getArgsType(),
            callStatement.getArgsValue());
    Result res;
    if (result instanceof Map){
        ((Map<?, ?>) result).remove("class");
        Object token = ((Map<?, ?>) result).get("token");
        if (ObjectUtil.isNotNull(token)) {
            res = Result.ok(token);
        } else {
            res = Result.ok(result);
        }
    } else {
        res = Result.ok(result);
    }
    handlerContext.getResponse().setBody(res);
}

    private GenericService getGenericService(DubboCallStatement callStatement, String uri) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(callStatement.getInterfaceName());
        reference.setUrl(uri);
        reference.setGeneric(GENERIC_SERIALIZATION_DEFAULT);
        reference.setGroup(callStatement.getInterfaceGroup());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("prefer.serialization", callStatement.getSerialization());
        parameters.put("serialization", callStatement.getSerialization());
        reference.setParameters(parameters);

        return reference.get();
    }
}
