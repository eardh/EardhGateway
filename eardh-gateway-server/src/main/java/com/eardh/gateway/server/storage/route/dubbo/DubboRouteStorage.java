package com.eardh.gateway.server.storage.route.dubbo;

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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eardh
 * @date 2023/4/3 16:25
 */
@Repository
@Slf4j
public class DubboRouteStorage  extends BaseStorage implements InitializingBean {

    private Map<String, DubboRoute> routes = new ConcurrentHashMap<>();

    public DubboRoute getDubboRoute(String apiId) {
        return routes.get(apiId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Result<JSONObject> result = GateUtils.httpGet(registry, "/wg/admin/rpc/dubbo-route-list", params, new TypeReference<>() {});
        List<JSONObject> items = result.getData().getList("items", JSONObject.class);
        for (JSONObject item : items) {
            DubboRoute dubboRoute = parseDubboRoute(item);
            routes.put(dubboRoute.getApiId(), dubboRoute);
        }
        messageListenerContainer.addMessageListener((message, pattern) -> {
            JSONObject jsonObject = JSON.parseObject(message.toString());
            DubboRoute dubboRoute = parseDubboRoute(jsonObject);
            log.info("DUBBO路由更新消息 {}", dubboRoute);
            if (StrUtil.isNotBlank(dubboRoute.getMicroserviceName())) {
                routes.put(dubboRoute.getApiId(), dubboRoute);
            }  else {
                routes.remove(dubboRoute.getApiId());
            }
        }, new PatternTopic(RedisKey.GATEWAY_DUBBO_ROUTES_CHANNEL_KEY));
    }

    private DubboRoute parseDubboRoute(JSONObject jsonObject) {
        DubboRoute dubboRoute = jsonObject.to(DubboRoute.class);
        String methodParametersType = jsonObject.getString("methodParametersType");
        String methodParametersName = jsonObject.getString("methodParametersName");
        dubboRoute.setResultType(jsonObject.getString("methodResultType"));
        if (StrUtil.isNotBlank(methodParametersType)) {
            String[] strings = methodParametersType.split(",");
            String[] strings1 = methodParametersName.split(",");
            dubboRoute.setParameterTypes(strings);
            dubboRoute.setParameterNames(strings1);
        } else {
            dubboRoute.setParameterTypes(new String[]{});
            dubboRoute.setParameterNames(new String[]{});
        }
        return dubboRoute;
    }
}
