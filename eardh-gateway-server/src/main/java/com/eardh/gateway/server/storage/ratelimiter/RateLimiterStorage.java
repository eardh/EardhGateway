package com.eardh.gateway.server.storage.ratelimiter;

import com.alibaba.fastjson2.JSON;
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author eardh
 * @date 2023/4/16 9:42
 */
@Repository
@Slf4j
public class RateLimiterStorage extends BaseStorage implements InitializingBean {

    private Map<String, ApiRateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    private List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();

    public ApiRateLimiter getRateLimiter(String apiId) {
        return rateLimiterMap.get(apiId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, "/wg/admin/api/rate_limiter-list", params, new TypeReference<>() {});
        List<ApiRateLimiter> items = result.getData().getList("items", ApiRateLimiter.class);
        for (ApiRateLimiter item : items) {
            rateLimiterMap.put(item.getApiId(), item);
        }
        messageListenerContainer.addMessageListener((message, pattern) -> {
            ApiRateLimiter apiRateLimiter = JSON.parseObject(message.toString(), ApiRateLimiter.class);
            log.info("限流表更新消息 {}", apiRateLimiter);
            for (Consumer<String> listener : listeners) {
                listener.accept(apiRateLimiter.getApiId());
            }
            rateLimiterMap.put(apiRateLimiter.getApiId(), apiRateLimiter);
        }, new PatternTopic(RedisKey.GATEWAY_RATE_LIMITER_CHANNEL_KEY));
    }

    public void addListener(Consumer<String> consumer) {
        listeners.add(consumer);
    }
}
