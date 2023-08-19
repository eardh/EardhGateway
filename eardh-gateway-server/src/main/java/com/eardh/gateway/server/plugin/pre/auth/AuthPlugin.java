package com.eardh.gateway.server.plugin.pre.auth;

import cn.hutool.core.util.StrUtil;
import com.eardh.gateway.common.util.JwtUtils;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.ErrorEnum;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.storage.api.GatewayApi;
import org.springframework.stereotype.Component;

/**
 * @author eardh
 * @date 2023/4/22 14:27
 */
@Component
public class AuthPlugin implements GatewayPlugin {

    @Override
    public boolean isSupport(HandlerContext handlerContext) {
        GatewayApi api = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
        return api.isApiAuth();
    }

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        Request request = handlerContext.getRequest();
        String token = request.getHeaders().get("token");
        try {
            if (StrUtil.isBlank(token) || !JwtUtils.verify(token)) {
                throw new GateWayException(ErrorEnum.TOKEN_HANDLER);
            }
        } catch (Exception e) {
            throw new GateWayException(ErrorEnum.TOKEN_HANDLER);
        }
        chain.doExecute(handlerContext);
    }

    @Override
    public int order() {
        return 7;
    }

    @Override
    public String getName() {
        return "authPlugin";
    }
}
