package com.eardh.gateway.server.storage.config;

import cn.hutool.core.util.StrUtil;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author eardh
 * @date 2023/4/17 16:10
 */
@Repository
@Slf4j
public class GlobalConfigStorage extends BaseStorage implements InitializingBean {

    private GlobalConfig globalConfig;

    public static final String GLOBAL_CONFIG_API = "/wg/admin/gateway/global-config";

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, GLOBAL_CONFIG_API, null, new TypeReference<>() {});
        this.globalConfig = parseJsonObject(result.getData());
        messageListenerContainer.addMessageListener((message, pattern) -> {
            JSONObject jsonObject = JSON.parseObject(message.toString());
            this.globalConfig = parseJsonObject(jsonObject);
            log.info("全局配置更新 {}", globalConfig);
        }, new PatternTopic(RedisKey.GLOBAL_CONFIG_KEY));
    }

    private GlobalConfig parseJsonObject(JSONObject object) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setRegistry(object.getString("registry"));
        globalConfig.setRedisAddress(object.getString("redisAddress"));
        String globalBlackList = object.getString("globalBlackList");
        Set<String> ipBlack = new HashSet<>();
        if (StrUtil.isNotBlank(globalBlackList)) {
            ipBlack.addAll(Arrays.asList(globalBlackList.split(",")));
        }
        globalConfig.setGlobalBlackList(ipBlack);
        return globalConfig;
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }
}
