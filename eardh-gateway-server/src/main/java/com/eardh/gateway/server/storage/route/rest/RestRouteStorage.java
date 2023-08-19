package com.eardh.gateway.server.storage.route.rest;

import cn.hutool.core.util.StrUtil;
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
 * @date 2023/4/14 14:16
 */
@Repository
@Slf4j
public class RestRouteStorage extends BaseStorage implements InitializingBean {

    private Map<String, RestRoute> routes = new ConcurrentHashMap<>();

    public RestRoute getRestRoute(String apiId) {
        return routes.get(apiId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, "/wg/admin/rpc/rest-route-list", params, new TypeReference<>() {});
        List<RestRoute> restRoutes = result.getData().getList("items", RestRoute.class);
        for (RestRoute route : restRoutes) {
            routes.put(route.getApiId(), route);
        }
        messageListenerContainer.addMessageListener((message, pattern) -> {
            RestRoute route = JSONUtil.toBean(message.toString(), RestRoute.class);
            log.info("REST路由更新消息 {}", route);
            if (StrUtil.isNotBlank(route.getMicroserviceName())) {
                routes.put(route.getApiId(), route);
            } else {
                routes.remove(route.getApiId());
            }
        }, new PatternTopic(RedisKey.GATEWAY_REST_ROUTES_CHANNEL_KEY));
    }
}
