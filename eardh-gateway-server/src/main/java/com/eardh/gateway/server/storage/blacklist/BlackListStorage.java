package com.eardh.gateway.server.storage.blacklist;

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
public class BlackListStorage extends BaseStorage implements InitializingBean {

    private Map<String, ApiBlackList> blackListMap = new ConcurrentHashMap<>();

    public ApiBlackList getBlackList(String apiId) {
        return blackListMap.get(apiId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, "/wg/admin/api/ip_black-list", params, new TypeReference<>() {});
        List<JSONObject> items = result.getData().getList("items", JSONObject.class);
        for (JSONObject item : items) {
            ApiBlackList apiBlackList = parseJsonObject(item);
            blackListMap.put(apiBlackList.getApiId(), apiBlackList);
        }
        messageListenerContainer.addMessageListener((message, pattern) -> {
            JSONObject jsonObject = JSON.parseObject(message.toString());
            ApiBlackList apiBlackList = parseJsonObject(jsonObject);
            log.info("黑名单更新消息 {}", apiBlackList);
            blackListMap.put(apiBlackList.getApiId(), apiBlackList);
        }, new PatternTopic(RedisKey.GATEWAY_IP_BLACKLIST_CHANNEL_KEY));
    }

    private ApiBlackList parseJsonObject(JSONObject jsonObject) {
        String apiId = jsonObject.getString("apiId");
        String blacklist = jsonObject.getString("ipBlacklist");
        Boolean forbidden = jsonObject.getBoolean("forbidden");
        Set<String> set = new HashSet<>();;
        if (StrUtil.isNotBlank(blacklist)) {
            set.addAll(Arrays.stream(blacklist.split(",")).toList());
        }
        return new ApiBlackList(apiId, set, forbidden);
    }
}
