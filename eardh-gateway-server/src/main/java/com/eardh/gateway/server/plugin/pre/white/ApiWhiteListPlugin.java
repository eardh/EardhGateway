package com.eardh.gateway.server.plugin.pre.white;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.whitelist.ApiWhiteList;
import com.eardh.gateway.server.storage.whitelist.WhiteListStorage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.eardh.gateway.core.exception.ErrorEnum.IP_WHITELIST_ERROR;
import static com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin.GATEWAY_API_KEY;

/**
 * @author eardh
 * @date 2023/4/15 16:11
 */
@Slf4j
@Component
public class ApiWhiteListPlugin implements GatewayPlugin {

    @Resource
    private WhiteListStorage whiteListStorage;

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        Request request = handlerContext.getRequest();
        GatewayApi api = handlerContext.getAttachment(GATEWAY_API_KEY);
        ApiWhiteList apiWhiteList = whiteListStorage.getWhiteList(api.getApiId());
        if (ObjectUtil.isNotNull(apiWhiteList) &&
                !apiWhiteList.getForbidden() &&
                !apiWhiteList.getIpWhitelist().contains(request.getClientIp())) {
            log.info("请求被拦截 请求路径{} 客户端IP{}", request.getUri(), request.getClientIp());
            throw new GateWayException(IP_WHITELIST_ERROR);
        }
        chain.doExecute(handlerContext);
    }

    @Override
    public int order() {
        return 6;
    }

    @Override
    public String getName() {
        return "whiteListPlugin";
    }

}
