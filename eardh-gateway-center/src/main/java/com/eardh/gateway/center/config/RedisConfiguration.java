package com.eardh.gateway.center.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.eardh.gateway.center.service.GlobalConfigService;
import com.eardh.gateway.common.util.GateUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.eardh.gateway.common.model.RedisKey.GLOBAL_CONFIG_KEY;

/**
 * @author eardh
 * @date 2023/4/14 21:39
 */
@Configuration
public class RedisConfiguration {

    @Resource
    private GlobalConfigService globalConfigService;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        String redisAddress = globalConfigService.getGlobalConfig().getRedisAddress();
        return new LettuceConnectionFactory(GateUtils.getIP(redisAddress), GateUtils.getPort(redisAddress));
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) throws NacosException {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericToStringSerializer<Object> stringSerializer = new GenericToStringSerializer<>(Object.class);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.afterPropertiesSet();

        globalConfigService.addListener(new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                GlobalConfig globalConfig = JSON.parseObject(configInfo, GlobalConfig.class);
                globalConfigService.setGlobalConfig(JSON.parseObject(configInfo, GlobalConfig.class));
                template.convertAndSend(GLOBAL_CONFIG_KEY, JSONUtil.toJsonStr(globalConfig));
            }
        });
        return template;
    }
}
