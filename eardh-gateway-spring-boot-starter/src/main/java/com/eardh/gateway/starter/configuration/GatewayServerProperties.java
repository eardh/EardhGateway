package com.eardh.gateway.starter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eardh
 * @date 2023/3/30 15:12
 */
@ConfigurationProperties(prefix = "eardh-gateway.gateway.server")
public class GatewayServerProperties {

    /** 网关port */
    private Integer port;

    private final Netty netty = new Netty();

    public Netty getNetty() {
        return netty;
    }

    public static class Netty {

        /**
         * sobacklog
         */
        private int so_backLog = 1024;

        /**
         * 聚合http请求
         */
        private int aggregator = 1000;

        /**
         * 处理io任务的线程数
         */
        private int bossGroupNThread = 1;

        /**
         * 处理工作任务的线程数
         */
        private int workerGroupNThread = Runtime.getRuntime().availableProcessors() * 2;

        public int getSo_backLog() {
            return so_backLog;
        }

        public void setSo_backLog(int so_backLog) {
            this.so_backLog = so_backLog;
        }

        public int getAggregator() {
            return aggregator;
        }

        public void setAggregator(int aggregator) {
            this.aggregator = aggregator;
        }

        public int getBossGroupNThread() {
            return bossGroupNThread;
        }

        public void setBossGroupNThread(int bossGroupNThread) {
            this.bossGroupNThread = bossGroupNThread;
        }

        public int getWorkerGroupNThread() {
            return workerGroupNThread;
        }

        public void setWorkerGroupNThread(int workerGroupNThread) {
            this.workerGroupNThread = workerGroupNThread;
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
