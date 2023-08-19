package com.eardh.gateway.server.storage.config;

import lombok.Data;

import java.util.Set;

/**
 * @author eardh
 * @date 2023/4/17 14:34
 */
@Data
public class GlobalConfig {

    /**
     * 注册中心
     */
    private String registry;

    /**
     * redis配置地址
     */
    private String redisAddress;

    /**
     * 全局黑名单列表
     */
    private Set<String> globalBlackList;

}
