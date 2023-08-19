package com.eardh.gateway.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.Map;

/**
 * @author eardh
 * @date 2023/4/2 10:28
 */
public class GateUtils {

    public static <P, T> T httpPost(String basePath, String uri, P object, TypeReference<T> typeReference) throws Exception {
        String resultStr = HttpUtil.post(basePath + uri, JSON.toJSONString(object), 3000);
        return JSON.parseObject(resultStr, typeReference);
    }

    public static <T> T httpGet(String basePath, String uri, Map<String, Object> params, TypeReference<T> typeReference) throws Exception {
        String resultStr = HttpUtil.get(basePath + uri, params, 3000);
        return JSON.parseObject(resultStr, typeReference);
    }

    public static String getProtocol(String uri) {
        String[] splits = uri.split("//");
        return splits[0].substring(0, splits[0].length() - 1);
    }

    public static String getAddress(String uri) {
        String[] splits = uri.split("//");
        return splits[1];
    }

    public static String getIP(String address) {
        String[] splits = address.split(":");
        return splits[0];
    }

    public static Integer getPort(String address) {
        String[] splits = address.split(":");
        return Integer.valueOf(splits[1]);
    }

    public static String buildUri(String protocol, String ip, Integer port) {
        return String.format("%s://%s:%d", protocol, ip, port);
    }
}
