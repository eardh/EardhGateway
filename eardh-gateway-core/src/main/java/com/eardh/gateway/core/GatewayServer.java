package com.eardh.gateway.core;

import com.eardh.gateway.core.server.HttpServer;

/**
 * @author eardh
 * @date 2023/3/25 16:03
 */
public class GatewayServer {

    private final HttpServer httpServer;

    public GatewayServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public void start() throws Exception {
        httpServer.start();
    }

    public void stop() throws Exception {
        httpServer.stop();
    }
}
