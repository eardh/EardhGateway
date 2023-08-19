package com.eardh.gateway.server.plugin.pre.ratelimiter;

import cn.hutool.core.util.ObjectUtil;
import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.exception.GateWayException;
import com.eardh.gateway.core.plugin.GatewayPlugin;
import com.eardh.gateway.core.plugin.GatewayPluginChain;
import com.eardh.gateway.server.plugin.pre.api.GatewayApiPlugin;
import com.eardh.gateway.server.plugin.pre.ratelimiter.extension.RedisRateLimiter;
import com.eardh.gateway.server.storage.api.GatewayApi;
import com.eardh.gateway.server.storage.ratelimiter.ApiRateLimiter;
import com.eardh.gateway.server.storage.ratelimiter.RateLimiterStorage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eardh.gateway.core.exception.ErrorEnum.RATE_LIMITER_ERROR;

/**
 * @author eardh
 * @date 2023/4/16 8:51
 */
@Component
@Slf4j
public class RateLimiterPlugin implements GatewayPlugin, InitializingBean {

    @Resource
    private RateLimiterStorage limiterStorage;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    Map<String, RedisRateLimiter> redisRateLimiterMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        limiterStorage.addListener(key -> {
            log.info("限流信息更新");
            redisRateLimiterMap.remove(key);
        });
    }

    @Override
public void doHandle(HandlerContext handlerContext, GatewayPluginChain chain) throws Exception {
    GatewayApi api = handlerContext.getAttachment(GatewayApiPlugin.GATEWAY_API_KEY);
    ApiRateLimiter rateLimiter = limiterStorage.getRateLimiter(api.getApiId());
    if (ObjectUtil.isNotNull(rateLimiter) && !rateLimiter.getForbidden()) {
        RedisRateLimiter redisRateLimiter = redisRateLimiterMap.get(rateLimiter.getApiId());
        if (ObjectUtil.isNull(redisRateLimiter)) {
            redisRateLimiter = new RedisRateLimiter(redisTemplate, rateLimiter);
            redisRateLimiterMap.put(rateLimiter.getApiId(), redisRateLimiter);
        }
        if (!redisRateLimiter.tryAcquire()) {
            throw new GateWayException(RATE_LIMITER_ERROR);
        }
    }
    chain.doExecute(handlerContext);
}

    @Override
    public int order() {
        return 1;
    }

    @Override
    public String getName() {
        return "rateLimiterPlugin";
    }
}
