package com.eardh.gateway.core.server.netty.config;

import lombok.Data;

/**
 * Netty容器启动时需要的配置
 *
 * @author eardh
 */
@Data
public class NettyConfig {

    /**
     * 端口
     */
    private int port;

    /**
     * sobacklog
     */
    private int so_backLog;

    /**
     * 聚合http请求
     */
    private int aggregator;

    /**
     * 处理io任务的线程数
     */
    private int bossGroupNThread;

    /**
     * 处理工作任务的线程数
     */
    private int workerGroupNThread;
}
