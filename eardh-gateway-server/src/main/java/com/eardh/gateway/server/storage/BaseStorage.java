package com.eardh.gateway.server.storage;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.HashMap;


public abstract class BaseStorage {

    @Value("${eardh-gateway.gateway.register.gateway-registry}")
    protected String registry;

    protected HashMap<String, Object> params = new HashMap<>(){{put("currentPage", -1);put("pageSize", -1);put("requireApiId", true);}};

    @Resource
    protected RedisMessageListenerContainer messageListenerContainer;

}
