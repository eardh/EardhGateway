package com.eardh.gateway.core.wapper;

import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Map;

/**
 * request 请求封装类
 *
 * @author eardh
 */
@ToString
public class Request {

    /**
     * 路径的uri
     */
    @Getter
    protected String uri;

    /**
     * 请求Id
     */
    @Getter
    protected String requestId;

    /**
     * 客户端Ip地址
     */
    @Getter
    protected String clientIp;

    /**
     * 请求方法 post get
     */
    @Getter
    protected String httpMethod;

    /**
     * 请求媒体类型 例如：application-json
     */
    @Getter
    protected String mediaType;

    /**
     * 请求头，key转为小写的
     */
    @Getter
    protected Map<String, String> headers;

    /**
     * 如果是原生Post请求，请求体 仅支持application-json,application-xml等格式
     * 也就是请求体
     */
    @Getter
    protected Map<String, Object> postBody;

    /**
     * 如果是get 请求,则拿到get请求的参数，全部转成String类型
     */
    @Getter
    protected Map<String, Object> params;

    /**
     * 构造requstWrapper
     *
     * @return 返回requestWrapper
     */
    protected Request buildRequestWrapper() throws IOException {
        throw new UnsupportedOperationException();
    }
}
