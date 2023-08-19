package com.eardh.gateway.common.model;

/**
 * @author eardh
 * @date 2023/4/7 10:23
 */
public interface RedisKey {

    String GATEWAY_NAMESPACE = "eardh-gateway";

    /**
     * 限流前缀
     */
    String REDIS_RATE_LIMITER_PREFIX = GATEWAY_NAMESPACE + ":redis-rate-limiter:";

    String GLOBAL_CONFIG_KEY = GATEWAY_NAMESPACE + ":global-config";

    /**
     * 发布API更新信息的通道
     */
    String GATEWAY_API_CHANNEL_KEY =  GATEWAY_NAMESPACE + ":channel:api";

    String GATEWAY_DUBBO_ROUTES_CHANNEL_KEY =  GATEWAY_NAMESPACE + ":channel:dubbo-route";

    String GATEWAY_REST_ROUTES_CHANNEL_KEY = GATEWAY_NAMESPACE + ":channel:rest-route";

    String GATEWAY_IP_WHITELIST_CHANNEL_KEY = GATEWAY_NAMESPACE + ":channel:ip-whitelist";

    String GATEWAY_IP_BLACKLIST_CHANNEL_KEY = GATEWAY_NAMESPACE + ":channel:ip-blacklist";

    String GATEWAY_RATE_LIMITER_CHANNEL_KEY = GATEWAY_NAMESPACE + ":channel:rate-limiter";
}
