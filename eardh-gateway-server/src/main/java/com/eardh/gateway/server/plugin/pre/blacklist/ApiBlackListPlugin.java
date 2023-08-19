package com.eardh.gateway.server.plugin.pre.blacklist;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.blacklist.ApiBlackList;
import com.eardh.gateway.server.storage.blacklist.BlackListStorage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.eardh.gateway.core.exception.ErrorEnum.IP_BLACKLIST_ERROR;
import static com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin.GATEWAY_API_KEY;

/**
 *  黑名单插件
 * @author eardh
 * @date 2023/4/15 15:29
 */
@Slf4j
@Component
public class ApiBlackListPlugin implements GatewayPlugin {

    @Resource
    private BlackListStorage blackListStorage;

    @Override
    public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
        Request request = handlerContext.getRequest();
        GatewayApi api = handlerContext.getAttachment(GATEWAY_API_KEY);
        ApiBlackList apiBlackList = blackListStorage.getBlackList(api.getApiId());
        if (ObjectUtil.isNotNull(apiBlackList) &&
                !apiBlackList.getForbidden() &&
                apiBlackList.getIpBlacklist().contains(request.getClientIp())) {
            log.info("请求被拦截 请求路径 {} 客户端IP {}", request.getUri(), request.getClientIp());
            throw new GateWayException(IP_BLACKLIST_ERROR);
        }
        chain.doExecute(handlerContext);
    }

    @Override
    public int order() {
        return 5;
    }

    @Override
    public String getName() {
        return "blackListPlugin";
    }
}
