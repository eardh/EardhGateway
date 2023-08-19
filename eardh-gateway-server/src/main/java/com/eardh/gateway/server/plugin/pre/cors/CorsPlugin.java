package com.eardh.gateway.server.plugin.pre.cors;

import cn.hutool.core.util.StrUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/23 14:13
 */
@Component
public class CorsPlugin implements GatewayPlugin {

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        if (StrUtil.equals(handlerContext.getRequest().getHttpMethod(), "options")) {
            configCors(handlerContext);
        } else {
            try {
                chain.doExecute(handlerContext);
            } catch (Exception e) {
                throw e;
            } finally {
                configCors(handlerContext);
            }
        }
    }
private void configCors(HandlerContext handlerContext) {
    Response response = handlerContext.getResponse();
    Map<String, String> headers = response.getHeaders();
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "*");
    headers.put("Access-Control-Allow-Methods", "*");
    headers.put("Access-Control-Allow-Credentials", "true");
}

    @Override
    public int order() {
        return -1;
    }

    @Override
    public String getName() {
        return "corsPlugin";
    }
}
