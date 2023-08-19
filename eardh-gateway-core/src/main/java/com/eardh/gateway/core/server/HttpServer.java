package com.eardh.gateway.core.server;

/**
 * HttpService 启动接口
 * @author eardh
 */
public interface HttpServer {

    /**
     * 初始化容器
     * @throws Exception e
     */
    void start() throws Exception;

    /**
     * 停止容器
     * @throws Exception 异常
     */
    void stop() throws Exception;
}
