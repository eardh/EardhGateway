package com.eardh.gateway.sdk.annotation.dubbo;

import java.lang.annotation.*;

/**
 * 方法参数名，参数如果没有注解，则使用默认的参数名
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ApiParam {

    /**
     * 方法参数名
     */
    String name();

}
