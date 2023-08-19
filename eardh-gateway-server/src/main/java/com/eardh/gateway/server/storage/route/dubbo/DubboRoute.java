package com.eardh.gateway.server.storage.route.dubbo;

import lombok.Data;

/**
 * @author eardh
 * @date 2023/3/26 20:43
 */
@Data
public class DubboRoute {

    /**
     * api标识
     */
    private String apiId;

    /**
     * 微服务名称
     */
    private String microserviceName;

    /** 微服务分组名称 */
    private String microserviceGroup;

    /**
     * 接口名称，类名
     */
    private String interfaceName;

    /** 接口所属组 */
    private String interfaceGroup;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数类型
     */
    private String[] parameterTypes;

    /**
     * 方法参数名
     */
    private String[] parameterNames;

    /**
     * 	返回值类型
     */
    private String resultType;

}
