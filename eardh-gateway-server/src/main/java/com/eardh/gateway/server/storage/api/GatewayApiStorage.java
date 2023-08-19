package com.eardh.gateway.server.storage.api;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.RedisKey;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import com.eardh.gateway.server.storage.BaseStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eardh
 * @date 2023/4/3 15:01
 */
@Repository
@Slf4j
public class GatewayApiStorage extends BaseStorage implements InitializingBean {

    private Map<String, GatewayApi> apiMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry,
                "/wg/admin/api/api-list", params, new TypeReference<>() {
                });
        List<GatewayApi> gatewayApis = result.getData().getList("items", GatewayApi.class);
        for (GatewayApi gatewayApi : gatewayApis) {
            apiMap.put(getKey(gatewayApi.getApiPath(), gatewayApi.getApiMethod()), gatewayApi);
        }
        messageListenerContainer.addMessageListener((message, pattern) -> {
            GatewayApi gatewayApi = JSONUtil.toBean(message.toString(), GatewayApi.class);
            log.info("网关API更新消息 {}", gatewayApi);
            apiMap.put(getKey(gatewayApi.getApiPath(), gatewayApi.getApiMethod()), gatewayApi);
        }, new PatternTopic(RedisKey.GATEWAY_API_CHANNEL_KEY));
    }

    public GatewayApi getGatewayApi(String path, String method) {
        return apiMap.get(getKey(path, method));
    }

    private String getKey(String path, String method) {
        return path + "#" + method;
    }
}
