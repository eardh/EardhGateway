package com.eardh.gateway.center.config;

import lombok.Data;

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

    private String globalBlackList;

}
