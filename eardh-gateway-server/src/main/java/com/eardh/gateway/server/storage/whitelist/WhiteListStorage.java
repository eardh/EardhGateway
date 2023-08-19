package com.eardh.gateway.server.storage.whitelist;

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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eardh
 * @date 2023/4/15 15:30
 */
@Slf4j
@Repository
public class WhiteListStorage extends BaseStorage implements InitializingBean {

    private Map<String, ApiWhiteList> whiteListMap = new ConcurrentHashMap<>();

    public ApiWhiteList getWhiteList(String apiId) {
        return whiteListMap.get(apiId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, "/wg/admin/api/ip_white-list", params, new TypeReference<>() {});
        List<JSONObject> items = result.getData().getList("items", JSONObject.class);
        for (JSONObject item : items) {
            ApiWhiteList apiWhiteList = parseJsonObject(item);
            whiteListMap.put(apiWhiteList.getApiId(), apiWhiteList
            );
        }

        messageListenerContainer.addMessageListener((message, pattern) -> {
            JSONObject jsonObject = JSON.parseObject(message.toString());
            ApiWhiteList apiWhiteList = parseJsonObject(jsonObject);
            log.info("白名单更新消息 {}", apiWhiteList);
            whiteListMap.put(apiWhiteList.getApiId(), apiWhiteList);
        }, new PatternTopic(RedisKey.GATEWAY_IP_WHITELIST_CHANNEL_KEY));
    }

    private ApiWhiteList parseJsonObject(JSONObject jsonObject) {
        String apiId = jsonObject.getString("apiId");
        String whitelist = jsonObject.getString("ipWhitelist");
        Boolean forbidden = jsonObject.getBoolean("forbidden");
        Set<String> set = new HashSet<>();;
        if (StrUtil.isNotBlank(whitelist)) {
            set.addAll(Arrays.stream(whitelist.split(",")).toList());
        }
        return new ApiWhiteList(apiId, set, forbidden);
    }
}
