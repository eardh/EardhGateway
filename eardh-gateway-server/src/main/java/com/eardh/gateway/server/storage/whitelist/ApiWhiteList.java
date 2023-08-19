package com.eardh.gateway.server.storage.whitelist;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @author eardh
 * @date 2023/4/15 15:32
 */
@AllArgsConstructor
@Data
public class ApiWhiteList {

    /**
     * API标识
     */
    private String apiId;

    /**
     * 白名单列表
     */
    private Set<String> ipWhitelist;

    /**
     * 是否禁止，0 - 不禁止；1 - 禁止
     */
    private Boolean forbidden;
}
