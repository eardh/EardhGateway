package com.eardh.gateway.sdk.annotation.rest;

import com.eardh.gateway.sdk.annotation.Api;

import java.lang.annotation.*;

/**
 * @author eardh
 * @date 2023/4/17 11:36
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RestService {

    /**
     * Rest描述
     */
    String description() default "";

    Api api();

}
