package com.eardh.gateway.core.exception.handler;

import com.eardh.gateway.core.HandlerContext;
import com.eardh.gateway.core.wapper.Response;

/**
 * 全局异常处理器
 *
 * @author eardh
 */
public interface ExceptionHandler {

    /**
     * 处理异常
     *
     * @param  e              e
     * @param  handlerContext 请求包装类
     * @return ResponseWrapper 返回响应包装类
     */
    Response handle(Exception e, HandlerContext handlerContext);
}
