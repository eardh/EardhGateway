package com.eardh.gateway.core.wapper;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * response 响应封装类
 * @author eardh
 */
@Data
public class Response {

    /**
     * 响应体类型
     */
    private String mediaType;

    /**
     * 响应头
     */
    private Map<String,String> headers = new HashMap<>();

    /**
     * 响应体
     */
    private Object body;

    /**
     * 后端服务的响应码：
     * 默认值 200
     */
    private Integer statusCode = 200;
}
