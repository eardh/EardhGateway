package com.eardh.gateway.server.plugin.post.header;

import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.util.Constant;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/3/27 16:42
 */
@Component
public class ResponseHeaderPlugin implements GatewayPlugin {

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        chain.doExecute(handlerContext);
        Map<String, String> headers = handlerContext.getResponse().getHeaders();
        headers.put(Constant.MEDIA_TYPE, Constant.APPLICATION_JSON);
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public String getName() {
        return "responseHeaderPlugin";
    }
}
