package com.eardh.gateway.sdk.annotation.dubbo;

import com.eardh.gateway.sdk.annotation.Api;

import java.lang.annotation.*;


/**
 * Api服务接口，含有该注解的类都会扫描注册方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DubboService {
    /**
     *  暴露的接口类，如果为空表示暴露实现的一个接口服务
     */
    Class<?> $interface() default DubboService.class;
    /**
     * 方法或接口描述
     */
    String description() default "";
    /**
     * 接口所属组
     */
    String group() default "default";
    /**
     * 对应的API注册信息
     */
    Api api() default @Api;
}
