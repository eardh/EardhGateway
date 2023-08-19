package com.eardh.gateway.server.storage.api;

import lombok.Data;

/**
 * @author eardh
 * @date 2023/4/14 16:46
 */
@Data
public class GatewayApi {

    /**
     * api标识
     */
    private String apiId;

    /**
     * api描述
     */
    private String apiDescription;

    /**
     * api路径
     */
    private String apiPath;

    /**
     * api方法，如http的get、post等
     */
    private String apiMethod;

    /**
     * api类型，rest、dubbo、tri等rpc协议
     */
    private String apiProtocol;

    /**
     * API是否认证
     */
    private boolean apiAuth;

}
