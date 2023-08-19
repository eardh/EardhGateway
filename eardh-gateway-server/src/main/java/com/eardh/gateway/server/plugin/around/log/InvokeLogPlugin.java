package com.eardh.gateway.server.plugin.around.log;

import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.storage.api.GatewayApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author eardh
 * @date 2023/4/7 21:49
 */
@Slf4j
@Component
public class InvokeLogPlugin implements GatewayPlugin {

    @Value("${eardh-gateway.gateway.register.gateway-registry}")
    protected String registry;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        long start = System.currentTimeMillis();
        try {
            chain.doExecute(handlerContext);
        } catch (Exception e) {
            throw e;
        } finally {
            long during = System.currentTimeMillis() - start;
            asyncInvokeLog(handlerContext, during);
        }
    }

    private void asyncInvokeLog(HandlerContext handlerContext, long during) {
        executorService.submit(() -> {
            HashMap<String, Object> map = new HashMap<>();
            GatewayApi gatewayApi = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
            map.put("apiId", gatewayApi.getApiId());
            map.put("invokerIp", handlerContext.getRequest().getClientIp());
            map.put("rpcInvokeTime", during);
            map.put("invokerUserInfo", null);
            try {
                GateUtils.httpPost(registry, "/wg/admin/api/api-invoke-log", map, new TypeReference<>() {});
                log.info("rpc调用日志记录成功");
            } catch (Exception e) {
                log.info("rpc调用日志记录失败");
            }
        });
    }

    @Override
    public int order() {
        return 1000;
    }

    @Override
    public String getName() {
        return "rpInvokeTimePlugin";
    }
}
