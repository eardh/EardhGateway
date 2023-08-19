package com.eardh.gateway.core;


import com.eardh.gateway.core.wapper.Request;
import com.eardh.gateway.core.wapper.Response;

/**
 * 网关执行上下文
 * @author eardh
 */
public interface HandlerContext {

    /**
     * 获取请求包装类
     * @return {@link Request}
     */
    Request getRequest();

    /**
     * 获取响应包装类
     * @return {@link Response}
     */
    Response getResponse();

    /**
     * 用于在handler之间可传递的多个公共配置
     */
    <T> T getAttachment(String name);

    <T> void setAttachment(String name, T attachment);

}
