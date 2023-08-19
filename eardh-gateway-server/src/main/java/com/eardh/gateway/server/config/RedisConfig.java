package com.eardh.gateway.server.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.eardh.gateway.common.model.Result;
import com.eardh.gateway.common.util.GateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.eardh.gateway.server.storage.config.GlobalConfigStorage.GLOBAL_CONFIG_API;

/**
 * @author eardh
 * @date 2023/4/7 9:42
 */
@Configuration
public class RedisConfig {

    @Value("${eardh-gateway.gateway.register.gateway-registry}")
    protected String registry;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, GLOBAL_CONFIG_API, null, new TypeReference<>() {});
        JSONObject jsonObject = result.getData();
        if (ObjectUtil.isNull(jsonObject)) {
            throw new RuntimeException();
        }
        String redisAddress = result.getData().getString("redisAddress");
        return new LettuceConnectionFactory(GateUtils.getIP(redisAddress), GateUtils.getPort(redisAddress));
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericToStringSerializer<Object> stringSerializer = new GenericToStringSerializer<>(Object.class);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
