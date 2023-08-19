package com.eardh.gateway.core;

import com.eardh.gateway.core.wapper.Response;
import com.eardh.gateway.core.wapper.Request;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的执行上下文
 * @author eardh
 */
public class DefaultHandlerExchange implements HandlerContext {

    /**
     * 请求包装类
     */
    @Setter
    private Request request;

    /**
     * 响应包装类
     */
    @Setter
    private Response response;

    /**
     * 处理器交换机的附件
     */
    private Map<String, Object> attachments = new ConcurrentHashMap<>();

    public DefaultHandlerExchange(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public <T> T getAttachment(String name) {
        return (T) attachments.get(name);
    }

    @Override
    public <T> void setAttachment(String name, T attachment) {
        attachments.put(name, attachment);
    }
}
