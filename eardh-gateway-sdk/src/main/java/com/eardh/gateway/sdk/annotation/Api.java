package com.eardh.gateway.sdk.annotation;

/**
 * @author eardh
 * @date 2023/4/20 10:27
 */
public @interface Api {

    /**
     * API路径
     */
    String path() default "";

    /**
     * API方法
     */
    HttpMethod method() default HttpMethod.get;

    /**
     * API描述
     */
    String description() default "";

    /**
     * 是否需要权限认证
     */
    boolean auth() default false;

    /**
     * 是否路由映射
     */
    boolean mapping() default true;

}
